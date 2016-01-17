package com.freelancer.xwatch.php;

import com.freelancer.xwatch.cli.*;
import com.freelancer.xwatch.runner.*;

public final class Install extends AbstractTask {
    private Option<String> db_hostname;
    private Option<String> db_username;
    private Option<String> db_password;
    private Option<String> db_database;
    private Option<String> db_driver;
    private Option<String> username;
    private Option<String> password;
    private Option<String> email;
    private Option<String> http_server;

    @Override
    public void initialize() {
        super.initialize();
        this.db_hostname = this.parser.addHelp(this.parser.addStringOption("db_hostname", true), "E.g: localhost");
        this.db_username = this.parser.addHelp(this.parser.addStringOption("db_username", true), "E.g: root");
        this.db_password = this.parser.addHelp(this.parser.addPasswordOption("db_password", true), "E.g: pass");
        this.db_database = this.parser.addHelp(this.parser.addStringOption("db_database", true), "E.g: opencart");
        this.db_driver = this.parser.addHelp(this.parser.addStringOption("db_driver", true), "E.g: mysqli");
        this.username = this.parser.addHelp(this.parser.addStringOption("username", true), "E.g: admin");
        this.password = this.parser.addHelp(this.parser.addPasswordOption("password", true), "E.g: admin");
        this.email = this.parser.addHelp(this.parser.addStringOption("email", true), "E.g: youremail@example.com");
        this.http_server =
            this.parser.addHelp(this.parser.addStringOption("http_server", true), "E.g: http://localhost/opencart");
    }

    @Override
    public int run(String[] args) throws Exception {
        return super.run(args);
    }

}
