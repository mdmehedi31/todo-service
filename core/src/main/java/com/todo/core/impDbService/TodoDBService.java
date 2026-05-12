package com.todo.core.impDbService;

import com.todo.core.dbservice.ITodoDBService;
import com.todo.core.entity.TodoEntity;
import com.todo.core.exceptionhandler.NotFoundException;
import com.todo.core.repository.ITodoRepository;
import com.todo.core.utils.DBUtils;
import com.todom.library.dto.DashBoardDTO;
import com.todom.library.dto.TodoListRequestDTO;
import com.todom.library.dto.TodoResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TodoDBService implements ITodoDBService {

    @Autowired
    private ITodoRepository todoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(TodoDBService.class);

    @Override
    public List<TodoEntity> getTodoByTitleAndCreatedById(String title,Integer createdBy) {
        try{
            List<TodoEntity> todoResponses= this.todoRepository.findAllByTitleAndCreatedById(title,createdBy);
            return todoResponses;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public TodoEntity getTodoById(Integer todoId) {
        try{
            Optional<TodoEntity> todoEntity = todoRepository.findByIdAndIsDeleted(todoId, false);

            if(!todoEntity.isPresent()){
                throw new NotFoundException("Todo with this id does not exist");
            }
            return todoEntity.get();
        } catch(NotFoundException e){
            throw e;
        }
        catch (Exception e) {
            throw  e;
        }
    }

    @Override
    public List<TodoEntity> getAllTodoByUserId(Integer createdById) {
        try{
            List<TodoEntity> todoEntities = this.todoRepository.findAllByCreatedById(createdById);
            return  todoEntities;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public TodoEntity addTodo(TodoEntity todoEntity) {
        try{
            TodoEntity savedTodo = this.todoRepository.save(todoEntity);
            return savedTodo;
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<TodoResponseDTO> getAllTodosByCreator(Integer userId) {
        try{

            String sql="select id id, created_on createdOn, goal_end_date goalEndDate, goal_start_date goalStartDate, todo_title title, todo_type todoType, sort_index sortIndex\n" +
                    "from "+ DBUtils.TB_TODO +" where created_by_id ="+userId+" and (is_deleted is null or is_deleted = false) order by sort_index asc;";
            log.info("Get all todos by creator SQL is : "+sql);

            List<TodoResponseDTO> todoList= jdbcTemplate.query(sql,new BeanPropertyRowMapper<TodoResponseDTO>(TodoResponseDTO.class));
            return todoList;
        } catch (Exception e) {
            throw e;
        }
    }


    public void getTotalDoneTodosAmount(LocalDate goalStartDate, LocalDate goalEndDate, Integer userId,DashBoardDTO dashBoardDTO){
        try{
            String sql ="select count(tdl.id) totalTodoItems, \n" +
                    "sum(if(tdl.is_done is null or tdl.is_done =0, 0,1)) totalDoneItems from tb_todo td inner join \n" +
                    "tb_todo_list tdl on td.id = tdl.todo_id\n" +
                    "WHERE (date(td.goal_start_date) >= date('"+goalStartDate+"') OR td.goal_start_date IS NULL)\n" +
                    "  AND ( date(td.goal_end_date) <= date('"+goalEndDate+"') OR td.goal_end_date IS NULL) \n" +
                    " and td.created_by_id = "+userId;

            log.info("Total done todos amount SQL is : "+sql);

            TodoResponseDTO todoList= jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<TodoResponseDTO>(TodoResponseDTO.class));

            if(todoList != null ){
                Integer totalDoneItems= todoList.getTotalDoneItems() !=null?todoList.getTotalDoneItems():0;
                Integer totalTodos= todoList.getTotalTodoItems() !=null ? todoList.getTotalTodoItems() :0;
                log.info("total done items : {}, total todos : {}", totalDoneItems, totalTodos);
                double percentageValue = (double) totalDoneItems / totalTodos;
                log.info("Percentage value: {}, percent is : {}", percentageValue, percentageValue*100);
                dashBoardDTO.setTotalDone(todoList.getTotalDoneItems());
                int percentage = (int) (percentageValue * 100);
                log.info("Percentage value is : {}", percentage);
                dashBoardDTO.setTotalDoneItemPercentage(percentage);
            }

        }catch (Exception e) {
            log.error("An exception throw : ",e);

        }
    }

    public Integer getTotalInProgressAmount(LocalDate goalStartDate, LocalDate goalEndDate, Integer userId){
        try{
            String sql="select td.id from tb_todo td inner join \n" +
                    "tb_todo_list tdl on td.id = tdl.todo_id\n" +
                    "WHERE (date(td.goal_start_date) >= date('"+goalStartDate+"') OR td.goal_start_date IS NULL)\n" +
                    "  AND ( date(td.goal_end_date) <= date('"+goalEndDate+"') OR td.goal_end_date IS NULL) \n" +
                    " and td.created_by_id = "+userId+
                    " group by tdl.todo_id\n" +
                    "having count(tdl.id)> count(tdl.is_done);";

            List<TodoResponseDTO> todoList= jdbcTemplate.query(sql,new BeanPropertyRowMapper<TodoResponseDTO>(TodoResponseDTO.class));

            if(todoList == null || todoList.isEmpty()){
                return 0;
            }
            return todoList.size();
        } catch (Exception e) {
            log.error("An exception thrown : ",e);
            return 0;
        }

    }


    public List<TodoResponseDTO> getAllOverDueItems(LocalDate goalStartDate, LocalDate goalEndDate, Integer userId){
        try{

            String sql ="select td.id id, td.todo_title title, td.goal_start_date goalStartDate, td.goal_end_date goalEndDate, count(tdl.id) totalTodoItems, \n" +
                    "sum(if(tdl.is_done is null or tdl.is_done =0, 0,1)) totalDoneItems from tb_todo td inner join \n" +
                    "tb_todo_list tdl on td.id = tdl.todo_id\n" +
                    "WHERE ((td.goal_start_date >= '"+goalStartDate+"' OR td.goal_start_date IS NULL)\n" +
                    "  AND (td.goal_end_date < '"+goalEndDate+"' OR td.goal_end_date IS NULL) )\n" +
                    " and td.created_by_id = "+userId+
                    " group by tdl.todo_id\n" +
                    "having count(tdl.id) > count(tdl.is_done);";
            List<TodoResponseDTO> todoList= jdbcTemplate.query(sql,new BeanPropertyRowMapper<TodoResponseDTO>(TodoResponseDTO.class));
            return todoList;
        }catch (Exception e){
            log.error("An exception thrown : ",e);
            return new ArrayList<>();
        }
    }

    public List<TodoResponseDTO> getRecentBoard(LocalDate goalStartDate, LocalDate goalEndDate, Integer userId){
        try{

            String sql ="select td.id id, td.todo_title title, td.goal_start_date goalStartDate, td.goal_end_date goalEndDate, count(tdl.id) totalTodoItems, \n" +
                    "sum(if(tdl.is_done is null or tdl.is_done =0, 0,1)) totalDoneItems from tb_todo td inner join \n" +
                    "tb_todo_list tdl on td.id = tdl.todo_id\n" +
                    "WHERE ((td.goal_start_date >= '"+goalStartDate+"' OR td.goal_start_date IS NULL)\n" +
                    "  AND (td.goal_end_date <= '"+goalEndDate+"' OR td.goal_end_date IS NULL) )\n" +
                    " and td.created_by_id = "+userId+
                    " group by tdl.todo_id ";

            List<TodoResponseDTO> todoList= jdbcTemplate.query(sql,new BeanPropertyRowMapper<TodoResponseDTO>(TodoResponseDTO.class));
            return todoList;
        }catch (Exception e){
            log.error("An exception thrown : ",e);
            return new ArrayList<>();
        }
    }


    public List<TodoResponseDTO> getOverDueTodos(LocalDate goalStartDate, LocalDate goalEndDate, Integer userId){
        try{

            String sql =" select td.todo_title title, td.goal_start_date goalStartDate, td.goal_end_date goalEndDate,count(tdl.id) totalTodoItems, \n" +
                    "sum(if(tdl.is_done is null or tdl.is_done =0, 0,1)) totalDoneItems from tb_todo td inner join \n" +
                    "tb_todo_list tdl on td.id = tdl.todo_id\n" +
                    "WHERE (date(td.goal_end_date) >= date('"+goalStartDate+"') OR td.goal_end_date IS NULL)\n" +
                    "  AND ( date(td.goal_end_date) <= date('"+goalEndDate+"') OR td.goal_end_date IS NULL) \n" +
                    " and td.created_by_id = "+userId +
                    " group by tdl.todo_id";

            List<TodoResponseDTO> todoList= jdbcTemplate.query(sql,new BeanPropertyRowMapper<TodoResponseDTO>(TodoResponseDTO.class));
            return todoList;
        }catch (Exception e){
            log.error("An exception thrown : ",e);
            return new ArrayList<>();
        }
    }


    @Override
    public DashBoardDTO getDashBoardValue(LocalDate goalStartDate, LocalDate goalEndDate,  Integer userId) {
        try{
            DashBoardDTO dashBoardDTO = new DashBoardDTO();
            Integer totalInprogress = getTotalInProgressAmount(goalStartDate, goalEndDate,userId);
            getTotalDoneTodosAmount(goalStartDate, goalEndDate,userId,dashBoardDTO);
            List<TodoResponseDTO> responseDTOS = getAllOverDueItems(goalStartDate, goalEndDate,userId);
            LocalDate today = LocalDate.now();
            LocalDate last21DaysBefore = today.minusDays(21);
            List<TodoResponseDTO> recentBoardDTOS = getRecentBoard(last21DaysBefore, today,userId);
            List<TodoResponseDTO> overDueTodos = getOverDueTodos(goalStartDate, goalEndDate,userId);
            dashBoardDTO.setTotalInprogress(totalInprogress);
            dashBoardDTO.setTodoResponseList(responseDTOS);
            dashBoardDTO.setRecentTodos(recentBoardDTOS);
            dashBoardDTO.setOverDueTodos(overDueTodos);

            return dashBoardDTO;
        }
catch (Exception e){
            log.error("An exception thrown : ",e);
    return null;
}

    }
}
