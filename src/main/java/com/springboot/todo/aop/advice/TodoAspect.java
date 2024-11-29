package com.springboot.todo.aop.advice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.springboot.todo.exception.FutureCreationDateException;
import com.springboot.todo.payload.TodoDto;

@Aspect
@Component
public class TodoAspect {
	
	private Logger logger = LogManager.getLogger(TodoAspect.class);
	
	private ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("[yyyy-mm-dd hh:mm:ss:SSS]");
		}
	};
	
	@Pointcut("execution(* com.springboot.todo.service.Todo*.create*(..)) && args(.., dto)")
	public void todoServiceCreationMethods(TodoDto dto) { }
	
	@Before("todoServiceCreationMethods(dto)")
	public void logTodoServiceCreationMethods(JoinPoint jp, TodoDto dto) {
		String methodName = jp.getSignature().getName();
		logger.info("+++++++++++++++++++++++++++++++++++Method name: " + methodName);
	}
	
	@Around("todoServiceCreationMethods(dto)")
	public Object resTrictTodoCreationForFutureDates(ProceedingJoinPoint jp, TodoDto dto) throws Throwable {
		String methodName = jp.getSignature().getName();
		logger.info("++++++++++ Method Name: " + methodName );
//		Object[] args = jp.getArgs();
		logger.info("Task: {}, Completed: {} and CompletionDate: {}", dto.getTask(), dto.isCompleted(), dto.getCreationDate());
		
		Date creationDate = dto.getCreationDate();
		if(creationDate.after(new Date())) {
			throw new FutureCreationDateException("Todo", "creationDate", creationDate);
		}
		
		Object proceed = jp.proceed();
		
		return proceed;
	}
}
