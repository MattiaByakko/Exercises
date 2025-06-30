package com.example.app2batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batch")
public class BatchUploadController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job pcJob;

    @Autowired
    private Job clienteJob;

    @Autowired
    private Job orderJob;

    @Autowired
    private Job orderItemJob;

    @Autowired
    private Job cartItemJob;

    @PostMapping("/import/pc")
    public String importPc() throws Exception {
        jobLauncher.run(pcJob, createJobParameters());
        return "PC import started!";
    }

    @PostMapping("/import/cliente")
    public String importCliente() throws Exception {
        jobLauncher.run(clienteJob, createJobParameters());
        return "Cliente import started!";
    }

    @PostMapping("/import/order")
    public String importOrder() throws Exception {
        jobLauncher.run(orderJob, createJobParameters());
        return "Order import started!";
    }

    @PostMapping("/import/order-item")
    public String importOrderItem() throws Exception {
        jobLauncher.run(orderItemJob, createJobParameters());
        return "OrderItem import started!";
    }

    @PostMapping("/import/cart-item")
    public String importCartItem() throws Exception {
        jobLauncher.run(cartItemJob, createJobParameters());
        return "CartItem import started!";
    }

    private JobParameters createJobParameters() {
        return new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
    }
}
