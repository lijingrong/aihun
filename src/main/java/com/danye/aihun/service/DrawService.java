package com.danye.aihun.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DrawService {


    public Integer draw(){
        //todo 先随机生成一个0-3的整数
        Random random = new Random();
        return random.nextInt(4);
    }
}
