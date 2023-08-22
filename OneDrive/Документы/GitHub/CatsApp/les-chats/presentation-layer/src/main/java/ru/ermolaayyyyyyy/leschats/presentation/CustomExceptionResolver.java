package ru.ermolaayyyyyyy.leschats.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import ru.ermolaayyyyyyy.leschats.servicelayer.exceptions.AccessDeniedException;
import ru.ermolaayyyyyyy.leschats.servicelayer.exceptions.EntityNotFoundException;
import ru.ermolaayyyyyyy.leschats.servicelayer.exceptions.InvalidAttributeException;

@Component
public class CustomExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        if (ex instanceof InvalidAttributeException) {
            modelAndView.setStatus(HttpStatus.BAD_REQUEST);
            modelAndView.addObject("message", ex.getMessage());
            return modelAndView;
        }
        if (ex instanceof EntityNotFoundException) {
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
            modelAndView.addObject("message", ex.getMessage());
            return modelAndView;
        }
        if (ex instanceof AccessDeniedException) {
            modelAndView.setStatus(HttpStatus.FORBIDDEN);
            modelAndView.addObject("message", ex.getMessage());
            return modelAndView;
        }
        return null;
    }
}
