package com.droplab.service;

import org.jsoup.Connection;

public interface BugsInfterface {
    boolean check();
    Connection.Response exploit();
    Object run(String type);
}
