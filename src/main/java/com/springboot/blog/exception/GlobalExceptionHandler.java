package com.springboot.blog.exception;

import com.springboot.blog.Dtos.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //1. handle specific exception
    @ExceptionHandler(ResourceNotFoundException.class)
   public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest){
        ErrorDetails details = new ErrorDetails(new Date(),
                exception.getMessage(),
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
   }

   //blogApi exception
  // 2. handle specific exception
    @ExceptionHandler(BlogAPIException.class)
    public ResponseEntity<ErrorDetails> handleBlogApiException(BlogAPIException exception,WebRequest webRequest){
          ErrorDetails errorDetails = new ErrorDetails(new Date(),exception.getMessage()
                  ,webRequest.getDescription(false),
                    HttpStatus.BAD_REQUEST);

          return  new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    //Handle GloBal Exception   like NumberFormat inputMisMath like all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(),exception.getMessage()
                ,webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR);

        return  new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }



    //overriding from ResponseEntityExceptionHandler class thats why extends

    //approach 1 to extends that class

    //customizing validation that are provide in dto
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error)->{
           String fieldName= ((FieldError)error).getField();
           String message=error.getDefaultMessage();

           errors.put(fieldName,message);
        });
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

    //Approach 2

    //inbuilt class
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//                                                                  WebRequest request) {
//        Map<String,String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error)->{
//            String fieldName= ((FieldError)error).getField();
//            String message=error.getDefaultMessage();
//
//            errors.put(fieldName,message);
//        });
//        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
//    }

    //handle exception for unAuthorise AccessDeniedException when  unauthorized user try to use

    //for security exception is this used

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException exception,WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(),exception.getMessage()
                ,webRequest.getDescription(false),
                HttpStatus.UNAUTHORIZED);

        return  new ResponseEntity<>(errorDetails,HttpStatus.UNAUTHORIZED);
    }


}
