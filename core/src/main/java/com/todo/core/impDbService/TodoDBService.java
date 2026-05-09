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


    public Integer getTotalDoneTodosAmount(LocalDate goalStartDate, LocalDate goalEndDate){
        try{
            String sql ="select td.id from tb_todo td inner join \n" +
                    "tb_todo_list tdl on td.id = tdl.todo_id\n" +
                    "WHERE (date(td.goal_start_date) >= date('"+goalStartDate+"') OR td.goal_start_date IS NULL)\n" +
                    "  AND ( date(td.goal_end_date) <= date('"+goalEndDate+"') OR td.goal_end_date IS NULL) \n" +
                    "group by tdl.todo_id\n" +
                    "having count(tdl.id)= count(tdl.is_done);";


            List<TodoResponseDTO> todoList= jdbcTemplate.query(sql,new BeanPropertyRowMapper<TodoResponseDTO>(TodoResponseDTO.class));

            if(todoList == null || todoList.isEmpty()){
                return 0;
            }
            return todoList.size();
        }catch (Exception e) {
            log.error("An exception throw : ",e);
            return 0;
        }
    }

    public Integer getTotalInProgressAmount(LocalDate goalStartDate, LocalDate goalEndDate){
        try{
            String sql="select td.id from tb_todo td inner join \n" +
                    "tb_todo_list tdl on td.id = tdl.todo_id\n" +
                    "WHERE (date(td.goal_start_date) >= date('"+goalStartDate+"') OR td.goal_start_date IS NULL)\n" +
                    "  AND ( date(td.goal_end_date) <= date('"+goalEndDate+"') OR td.goal_end_date IS NULL) \n" +
                    "group by tdl.todo_id\n" +
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


    public List<TodoResponseDTO> getAllOverDueItems(LocalDate goalStartDate, LocalDate goalEndDate){
        try{

            String sql ="select td.id id, td.todo_title title, td.goal_start_date goalStartDate, td.goal_end_date goalEndDate, count(tdl.id) totalTodoItems, \n" +
                    "sum(if(tdl.is_done is null or tdl.is_done =0, 0,1)) totalDoneItems from tb_todo td inner join \n" +
                    "tb_todo_list tdl on td.id = tdl.todo_id\n" +
                    "WHERE ((td.goal_start_date >= '"+goalStartDate+"' OR td.goal_start_date IS NULL)\n" +
                    "  AND (td.goal_end_date < '"+goalEndDate+"' OR td.goal_end_date IS NULL) )\n" +
                    "group by tdl.todo_id\n" +
                    "having count(tdl.id) > count(tdl.is_done);";
            List<TodoResponseDTO> todoList= jdbcTemplate.query(sql,new BeanPropertyRowMapper<TodoResponseDTO>(TodoResponseDTO.class));
            return todoList;
        }catch (Exception e){
            log.error("An exception thrown : ",e);
            return new ArrayList<>();
        }
    }

    public List<TodoResponseDTO> getRecentBoard(LocalDate goalStartDate, LocalDate goalEndDate){
        try{

            String sql ="select td.id id, td.todo_title title, td.goal_start_date goalStartDate, td.goal_end_date goalEndDate, count(tdl.id) totalTodoItems, \n" +
                    "sum(if(tdl.is_done is null or tdl.is_done =0, 0,1)) totalDoneItems from tb_todo td inner join \n" +
                    "tb_todo_list tdl on td.id = tdl.todo_id\n" +
                    "WHERE ((td.goal_start_date >= '"+goalStartDate+"' OR td.goal_start_date IS NULL)\n" +
                    "  AND (td.goal_end_date <= '"+goalEndDate+"' OR td.goal_end_date IS NULL) )\n" +
                    "group by tdl.todo_id ";

            List<TodoResponseDTO> todoList= jdbcTemplate.query(sql,new BeanPropertyRowMapper<TodoResponseDTO>(TodoResponseDTO.class));
            return todoList;
        }catch (Exception e){
            log.error("An exception thrown : ",e);
            return new ArrayList<>();
        }
    }

    @Override
    public DashBoardDTO getDashBoardValue(LocalDate goalStartDate, LocalDate goalEndDate) {
        try{
            DashBoardDTO dashBoardDTO = new DashBoardDTO();
            Integer totalInprogress = getTotalInProgressAmount(goalStartDate, goalEndDate);
            Integer totalDone = getTotalDoneTodosAmount(goalStartDate, goalEndDate);
            List<TodoResponseDTO> responseDTOS = getAllOverDueItems(goalStartDate, goalEndDate);
            LocalDate today = LocalDate.now();
            LocalDate last21DaysBefore = today.minusDays(21);
            List<TodoResponseDTO> recentBoardDTOS = getRecentBoard(last21DaysBefore, today);

            dashBoardDTO.setTotalInprogress(totalInprogress);
            dashBoardDTO.setTotalDone(totalDone);
            dashBoardDTO.setTodoResponseList(responseDTOS);
            dashBoardDTO.setRecentTodos(recentBoardDTOS);

            return dashBoardDTO;
        }
catch (Exception e){
            log.error("An exception thrown : ",e);
    return null;
}

    }
}
