package com.todo.core.impDbService;

import com.todo.core.dbservice.ITodoDBService;
import com.todo.core.entity.TodoEntity;
import com.todo.core.exceptionhandler.NotFoundException;
import com.todo.core.repository.ITodoRepository;
import com.todo.core.utils.DBUtils;
import com.todom.library.dto.TodoResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
}
