package open.seats.tracker.exception;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.log4j.Log4j2;
import open.seats.tracker.response.ApiResponse;

@Log4j2
@ControllerAdvice
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody ApiResponse handleException(HttpServletResponse response, ValidationException ex) {
		log.error("Global validation exception handled! Message = {}", ex.getMessage());
		return new ApiResponse(ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ApiResponse handleException(Exception ex) {
		log.error("new Global exception handled! Message = {}", ex);
		return new ApiResponse("Oops! Something went wrong. Please try again later.");
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		log.error("Global NoHandlerFoundException handled! Message = {}", ex.getMessage());
		return new ResponseEntity(new ApiResponse("Invalid Request! No Handler Found"), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		log.error("Global BindException handled! Message = {}", ex.getMessage());
		return new ResponseEntity(new ApiResponse("Invalid Request! Bind Not Found"), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("Global ConversionNotSupportedException handled!  Message = {}", ex.getMessage());
		return new ResponseEntity(new ApiResponse("Invalid Request! Conversion Not Supported"), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		log.error("Global internal exception handled! Message = {}", ex.getMessage());
		return new ResponseEntity(new ApiResponse("Invalid Request! Internal Exception"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("Global HttpMediaTypeNotAcceptableException handled! Message = {}", ex.getMessage());
		return new ResponseEntity(new ApiResponse("Invalid Request! Media Type Not Acceptable"), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("Global HttpMediaTypeNotSupportedException handled! Message = {}", ex.getMessage());
		return new ResponseEntity(new ApiResponse("Invalid Request! Media Type Not Supported"), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("Global HttpMessageNotReadableException handled! Message = {}", ex.getMessage());
		return new ResponseEntity(new ApiResponse("Invalid Request! Request Not Readable. Please provide a valid JSON input"), HttpStatus.BAD_REQUEST);

	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("Global HttpMessageNotWritableException handled! Message = {}", ex.getMessage());
		return new ResponseEntity(new ApiResponse("Oops! Something went wrong. Please try again later"), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error("Global HttpRequestMethodNotSupportedException handled! Message = {}" + ex.getMessage(), ex);
		return new ResponseEntity(new ApiResponse("Invalid Request! Request Method Not Supported"), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("Global MethodArgumentNotValidException handled! Message = {}", ex.getMessage());
		return new ResponseEntity(new ApiResponse(ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage()), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("Global MissingServletRequestParameterException handled! Message = {}", ex.getMessage());
		return new ResponseEntity(new ApiResponse("Invalid Request! Missing Request Parameter"), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("Global MissingServletRequestParameterException handled! Message = {}", ex.getMessage());
		return new ResponseEntity(new ApiResponse("Invalid Request! Missing Request Part"), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("Global ServletRequestBindingException handled! Message = {}", ex.getMessage());
		return new ResponseEntity(new ApiResponse("Invalid Request! Unable to bind Request"), HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		log.error("Global ServletRequestBindingException handled! Message = {}", ex.getMessage());
		return new ResponseEntity(new ApiResponse("Invalid Request! Type Mismatch"), HttpStatus.BAD_REQUEST);
	}

}
