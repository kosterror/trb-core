package ru.hits.trb.trbcore.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        log.info("Execution request started: {} {}",
                request.getMethod(),
                request.getRequestURI()
        );
        filterChain.doFilter(request, response);
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("Execution request finished: {} {} in {} ms",
                request.getMethod(),
                request.getRequestURI(),
                executionTime
        );
    }

}

