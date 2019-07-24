package com.example.kgapp

public class UserMessage {
    var msg: String
    var sender: User
    var createdAt: String
    var receiver: String

    constructor(msg: String, sender: User, createdAt: String, receiver: String) {
        this.msg = msg
        this.sender = sender
        this.createdAt = createdAt
        this.receiver = receiver
    }

}