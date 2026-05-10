package com.todo.service.todoservice;

import com.todo.core.dbservice.ITodoDBService;
import com.todo.core.dbservice.ITodoListDBService;
import com.todo.core.entity.TodoEntity;
import com.todo.core.entity.TodoListEntity;
import com.todo.core.exceptionhandler.InvalidInputException;
import com.todo.core.exceptionhandler.NotFoundException;
import com.todom.library.dto.*;
import com.todom.library.enums.SuccessOrFailedEnum;
import com.todom.library.enums.TodoType;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class TodoService implements ITodoService{

    @Autowired
    private ITodoDBService todoDBService;

    @Autowired
    private ITodoListDBService todoListDBService;

    private final Logger log= LoggerFactory.getLogger(TodoService.class);

    @Override
    public TodoResponseDTO createTodo(TodoRequestDTO todoRequestDTO) {
        try{
            if(todoRequestDTO!=null && (todoRequestDTO.getTitle()==null || todoRequestDTO.getTitle().isBlank())){
                throw new InvalidInputException("Title  can't be blank");
            }
            List<TodoEntity> todosExist = this.todoDBService.getTodoByTitleAndCreatedById(todoRequestDTO.getTitle(), todoRequestDTO.getCreatedById());

            if(todosExist!=null && todosExist.size()>0){
                Integer lastIndexValue = todosExist.size();
                String newCount = lastIndexValue.toString();
                if (lastIndexValue<10) {
                    newCount = "0"+newCount;
                }
                String newTitle = "Copy title no : "+newCount+" - "+todoRequestDTO.getTitle();
                todoRequestDTO.setTitle(newTitle);
            }

            List<TodoEntity> todoEntityList = this.todoDBService.getAllTodoByUserId(todoRequestDTO.getCreatedById());
            Integer sortIndex=1;
            if(todoEntityList != null && todoEntityList.size()>0){
                log.debug("When todo entity list is not null and size is grater the 0");
                sortIndex=todoEntityList.size()+1;
                log.debug("Sort index value is : {}", sortIndex);
            }
            TodoEntity todoEntity = new TodoEntity();
            todoEntity.setCreatedOn(Instant.now());
            todoEntity.setSortIndex(sortIndex);
            todoEntity.setIsDeleted(false);
            TodoEntity convertedTodoEntity = todoRequestToEntity(todoRequestDTO, todoEntity);
            TodoEntity saveResponse =this.todoDBService.addTodo(convertedTodoEntity);

            if(saveResponse!=null){
                log.info("When save response is not null... ");
                if(todosExist !=null && todosExist.size()>0){
                    log.debug("When todos exist is not null and size is grater then zero");
                    TodoEntity todoUpdateEntity=  todosExist.get(0);
                    this.todoDBService.addTodo(todoUpdateEntity);
                }

                TodoResponseDTO todoResponseDTO = convertTodoEntityToTodoResponseDTO(saveResponse);
                return todoResponseDTO;
            }
            return new TodoResponseDTO();
        } catch (InvalidInputException e) {
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<TodoResponseDTO> getAllTodoByCreatedById(Integer createdById) {
       try{
           List<TodoResponseDTO> todoList= this.todoDBService.getAllTodosByCreator(createdById);
           return todoList;
       } catch (Exception e) {
           throw e;
       }
    }

    @Override
    public TodoResponseDTO updateTodo(TodoRequestDTO todoRequestDTO) {
        try {

            if(todoRequestDTO.getId() ==null){
                throw new InvalidInputException("Todo id can't be empty...");
            }
            TodoEntity todoEntity = this.todoDBService.getTodoById(todoRequestDTO.getId());
            if(todoEntity==null){
                throw new NotFoundException("Todo not found with this");
            }
            TodoEntity convertedEntity = todoRequestToEntity(todoRequestDTO, todoEntity);
            convertedEntity = this.todoDBService.addTodo(convertedEntity);
            TodoResponseDTO responseDTO = convertTodoEntityToTodoResponseDTO(convertedEntity);
            return responseDTO;
        } catch (NotFoundException e) {
            throw e;
        }
        catch (InvalidInputException e) {
            throw e;
        }
        catch (Exception e) {
            throw  e;
        }
    }

    @Override
    public ApiResponse deleteTodo(Integer todoId) {
        try{
            log.debug("Delete todo id is {}", todoId);
            TodoEntity todoEntity = this.todoDBService.getTodoById(todoId);
            if(todoEntity == null){
                throw new NotFoundException("Todo not found with this id");
            }
            log.debug("Todo entity found value is : {}", todoEntity.getTitle());
            todoEntity.setIsDeleted(true);
            TodoEntity todoUpdateResponse= this.todoDBService.addTodo(todoEntity);
            if(todoUpdateResponse !=null){
                boolean isUpdated= this.todoListDBService.deleteTodoListByTodoId(todoId);
            }
            return new ApiResponse(SuccessOrFailedEnum.SUCCESS);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    // Todo list from here
    @Override
    public TodoListRequestDTO createTodoList(TodoListRequestDTO todoListRequestDTO) {
        try{
            log.info("When createing todolist");
            if(todoListRequestDTO.getTodoId()==null){
                throw new InvalidInputException("Todo id can't be empty.");
            }
            if(todoListRequestDTO.getTitle()==null || todoListRequestDTO.getTitle().isBlank()){
                throw new InvalidInputException("Title can't be empty.");
            }

            TodoEntity todoEntity= this.todoDBService.getTodoById(todoListRequestDTO.getTodoId());
            if(todoEntity==null){
              throw new NotFoundException("Todo not found with this id");
            }
            /*List<TodoListEntity> todoListEntityList=this.todoListDBService.getTodoListByTodoIdAndTitle(todoListRequestDTO.getTodoId(), todoListRequestDTO.getTitle());
            if(todoListEntityList!=null && todoListEntityList.size()>0){
                Integer lastIndexValue =todoListEntityList.size()+1;
                String newTitle = "Copy todo list title no : "+lastIndexValue+" "+todoListRequestDTO.getTitle();
                todoListRequestDTO.setTitle(newTitle);
            }*/
            List<TodoListEntity> todoListEntites = this.todoListDBService.getTodoListByTodoListId(todoListRequestDTO.getTodoId());

            Integer sortOrder =1;
            if(todoListEntites!=null && todoListEntites.size()>0){
                sortOrder=todoListEntites.size()+1;
            }

            TodoListEntity todoListEntity = new TodoListEntity();
            todoListEntity.setCreatedOn(Instant.now());
            todoListEntity.setTodoId(todoListRequestDTO.getTodoId());
            todoListEntity.setCreatedBy(todoListRequestDTO.getCreatedBy());
            todoListEntity.setSortOrder(sortOrder);
            todoListEntity= convertTodoListRequestToEntity(todoListEntity,todoListRequestDTO);
            TodoListEntity saveTodoList = this.todoListDBService.createTodoList(todoListEntity);
            if(saveTodoList!=null){
                TodoListRequestDTO  todoListResponseDTO= convertTodoListEntityToTodoResponseDTO(saveTodoList);
                return todoListResponseDTO;
            }
            return new TodoListRequestDTO();
        } catch (NotFoundException e) {
            throw e;
        } catch (InvalidInputException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public TodoListRequestDTO updateTodoList(TodoListRequestDTO todoListRequestDTO) {
        try{
            if(todoListRequestDTO.getId() ==null){
                throw new InvalidInputException("Todo list id can't be empty");
            }

            log.info("Update todolist ");
            TodoListEntity todoListEntity = this.todoListDBService.getTodoListById(todoListRequestDTO.getId());
            todoListEntity = convertTodoListRequestToEntity(todoListEntity,todoListRequestDTO);
            TodoListEntity saveTodoList = this.todoListDBService.updateTodoList(todoListEntity);
            if(saveTodoList!=null){
                return convertTodoListEntityToTodoResponseDTO(saveTodoList);
            }
            return  null;
        } catch (Exception e) {
            throw  e;
        }
    }

    @Override
    public ApiResponse deleteTodoList(Integer todoListId) {
        try{
            TodoListEntity todoListEntity = this.todoListDBService.getTodoListById(todoListId);

            if(todoListEntity==null){
                throw new NotFoundException("Todo list with this id is not exist");
            }

            todoListEntity.setIsDeleted(true);
            this.todoListDBService.updateTodoList(todoListEntity);
            return new ApiResponse(SuccessOrFailedEnum.SUCCESS);
        }catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<TodoListRequestDTO> getAllTodoListByTodoId(Integer todoId) {
        try {
            return this.todoListDBService.getTodoListByTodoId(todoId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(rollbackOn = {Exception.class, InvalidInputException.class})
    @Override
    public ApiResponse addAiGeneratedTodoList(TodoDTO todoDTO, Integer createdBy) {
        try{
            if(todoDTO==null || todoDTO.getTitle()==null || todoDTO.getTitle().isBlank()){
                throw new InvalidInputException("Todo can't be empty");
            }

            List<TodoEntity> todoEntityList = this.todoDBService.getAllTodoByUserId(createdBy);
            Integer sortIndex=1;
            if(todoEntityList != null && todoEntityList.size()>0){
                log.debug("When todo entity list is not null and size is grater the 0");
                sortIndex=todoEntityList.size()+1;
                log.debug("Sort index value is : {}", sortIndex);
            }

            TodoEntity todoEntity= new TodoEntity();
            todoEntity.setTitle(todoDTO.getTitle());
            todoEntity.setTodoType(TodoType.AI_GENERATED);
            todoEntity.setSortIndex(sortIndex);
            todoEntity.setCreatedOn(Instant.now());
            todoEntity.setIsDeleted(false);
            todoEntity.setCreatedById(createdBy);
            todoEntity.setGoalStartDate(Instant.now());
            todoEntity.setGoalEndDate(Instant.now().plus(10, ChronoUnit.DAYS));


            todoEntity= this.todoDBService.addTodo(todoEntity);

            if(todoEntity!=null){

                List<TodoListEntity> todoListEntityList = new ArrayList<>();
                int sortOrder =1;
                for (String items : todoDTO.getTodoList()){

                    TodoListEntity todoListEntity = new TodoListEntity();
                    todoListEntity.setTodoId(todoEntity.getId());
                    todoListEntity.setCreatedBy(createdBy);
                    todoListEntity.setCreatedOn(Instant.now());
                    todoListEntity.setTitle(items);
                    todoListEntity.setSortOrder(sortOrder);
                    todoListEntity.setIsDeleted(false);
                    sortIndex++;
                    todoListEntityList.add(todoListEntity);
                }
                this.todoListDBService.addAllTOdoLists(todoListEntityList);
                return new ApiResponse(SuccessOrFailedEnum.SUCCESS);
            }

            return new ApiResponse(SuccessOrFailedEnum.FAILED);
        }catch (Exception e) {
            log.error("An Exception thrown : ",e);
            return new ApiResponse(SuccessOrFailedEnum.FAILED);
        }

    }

    @Override
    public DashBoardDTO getDashboardDataWithinDate(LocalDate startDate, LocalDate endDate,  Integer userId) {
        try{

            return  this.todoDBService.getDashBoardValue(startDate, endDate,userId);
        }catch (Exception e){
            log.error("An Exception thrown : ",e);
        }

        return null;
    }

    private TodoListRequestDTO  convertTodoListEntityToTodoResponseDTO(TodoListEntity todoListEntity) {
        TodoListRequestDTO responseDTO = new TodoListRequestDTO();
        responseDTO.setId(todoListEntity.getId());
        responseDTO.setTodoId(todoListEntity.getTodoId());
        responseDTO.setTitle(todoListEntity.getTitle());
        responseDTO.setCreatedBy(todoListEntity.getCreatedBy());
        responseDTO.setCreatedOn(todoListEntity.getCreatedOn());
        responseDTO.setTodoBgColor(todoListEntity.getTodoBgColor());
        responseDTO.setSortOrder(todoListEntity.getSortOrder());
        return responseDTO;
    }
    private TodoListEntity convertTodoListRequestToEntity(TodoListEntity todoListEntity, TodoListRequestDTO todoListRequestDTO) {

        if(todoListRequestDTO!=null && todoListRequestDTO.getTitle()!=null && !todoListRequestDTO.getTitle().isBlank()){
            todoListEntity.setTitle(todoListRequestDTO.getTitle());
        }

        if(todoListRequestDTO !=null && todoListRequestDTO.getTodoBgColor()!=null && !todoListRequestDTO.getTodoBgColor().isBlank()){
            todoListEntity.setTodoBgColor(todoListRequestDTO.getTodoBgColor());
        }

        todoListEntity.setIsDone(todoListRequestDTO.getIsDone());

        return todoListEntity;
    }

    private TodoResponseDTO convertTodoEntityToTodoResponseDTO(TodoEntity todoEntity) {
        try{
            TodoResponseDTO todoResponseDTO = new TodoResponseDTO();
            todoResponseDTO.setId(todoEntity.getId());
            todoResponseDTO.setTitle(todoEntity.getTitle());
            todoResponseDTO.setCreatedOn(todoEntity.getCreatedOn());
            todoResponseDTO.setGoalStartDate(todoEntity.getGoalStartDate());
            todoResponseDTO.setGoalEndDate(todoEntity.getGoalEndDate());
            todoResponseDTO.setTodoType(todoEntity.getTodoType());
            todoResponseDTO.setCreatedById(todoEntity.getCreatedById());
            todoResponseDTO.setSortIndex(todoEntity.getSortIndex());
            todoResponseDTO.setIsDeleted(todoEntity.getIsDeleted());

            return todoResponseDTO;
        } catch (Exception e) {
            throw e;
        }
    }

    private TodoEntity todoRequestToEntity(TodoRequestDTO todoRequestDTO, TodoEntity todoEntity) {

        if(todoRequestDTO.getTitle()!=null && !todoRequestDTO.getTitle().isBlank()){
            todoEntity.setTitle(todoRequestDTO.getTitle());
        }

        if (todoRequestDTO.getTodoType()!=null){
            todoEntity.setTodoType(todoRequestDTO.getTodoType());
        }

        if(todoRequestDTO.getGoalStartDate()!=null){
            todoEntity.setGoalStartDate(todoRequestDTO.getGoalStartDate());
        }
        if(todoRequestDTO.getGoalEndDate()!=null){
            todoEntity.setGoalEndDate(todoRequestDTO.getGoalEndDate());
        }
        if (todoRequestDTO.getCreatedById()!=null){
            todoEntity.setCreatedById(todoRequestDTO.getCreatedById());
        }
        return todoEntity;
    }
}
