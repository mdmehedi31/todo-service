package com.todo.core.impDbService;

import com.todo.core.dbservice.ITodoListDBService;
import com.todo.core.entity.TodoListEntity;
import com.todo.core.repository.ITodoListRepository;
import com.todo.core.utils.DBUtils;
import com.todom.library.dto.TodoListResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoListDBService implements ITodoListDBService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ITodoListRepository todoListRepository;

    private static final Logger log = LoggerFactory.getLogger(TodoListDBService.class);
    @Override
    public TodoListEntity createTodoList(TodoListEntity todoListEntity) {
        try{
            TodoListEntity createdTodoList = todoListRepository.save(todoListEntity);
            return createdTodoList;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public TodoListEntity updateTodoList(TodoListEntity todoListEntity) {
        return this.todoListRepository.save(todoListEntity);
    }

    @Override
    public TodoListEntity deleteTodoList(TodoListEntity todoListEntity) {
        return null;
    }

    @Override
    public List<TodoListResponseDTO> getTodoListByTodoId(Integer todoId) {
        try{
            String sql = "select id id,todo_id todoId, title title, is_deleted isDeleted,\n" +
                    "todo_bg_color todoBgColor,created_by createdBy, sort_order sortOrder,created_on createdOn" +
                    " from "+ DBUtils.TB_TODO_LIST +" where todo_id ="+todoId+" and is_deleted=0;";
            List<TodoListResponseDTO> todoListResponse = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TodoListResponseDTO.class));

            log.info("the sql is : "+sql);
            return todoListResponse;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<TodoListEntity> getTodoListByTodoListId(Integer todoListId) {
        try{
            List<TodoListEntity> todoListEntityList= todoListRepository.findAllByTodoId(todoListId);
            return todoListEntityList;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<TodoListEntity> getTodoLists() {
        return List.of();
    }

    @Override
    public List<TodoListEntity> getTodoListByTodoIdAndTitle(Integer todoListId, String title) {
       try{
           List<TodoListEntity> todoListEntityList= this.todoListRepository.getTodoListEntityByTodoIdAndTitleAndIsDeleted(todoListId, title,false);
           return todoListEntityList;
       } catch (Exception e) {
           throw e;
       }
    }

    @Override
    public TodoListEntity getTodoListById(Integer todoListId) {
        try{
            TodoListEntity todoListEntity= todoListRepository.findByIdAndIsDeleted(todoListId);
            return todoListEntity;
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean deleteTodoListByTodoId(Integer todoId) {
        try{
            String sql = "update tb_todo_list set is_deleted = 0 where todo_id = "+todoId;
            jdbcTemplate.update(sql);
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean addAllTOdoLists(List<TodoListEntity> todoListEntities) {
        try{
            this.todoListRepository.saveAll(todoListEntities);
            return true;
        }catch (Exception e) {
            throw e;
        }
    }
}
