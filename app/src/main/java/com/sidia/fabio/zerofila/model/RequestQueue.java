package com.sidia.fabio.zerofila.model;


public class RequestQueue {
    public String cpf;
    public String name;
    public String establishmentKey;
    public String clerkName;
    public String clerkKey;

    public RequestQueue(String cpf, String name, String establishmentKey, String clerkName, String clerkKey) {
        this.cpf = cpf;
        this.establishmentKey = establishmentKey;
        this.clerkName = clerkName;
        this.clerkKey = clerkKey;
        this.name = name;
    }
}
