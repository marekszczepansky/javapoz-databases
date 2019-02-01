package pl.szczepanski.marek.demo.databases;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class BaseController {

    private JDBCService jdbcService;

    public BaseController(JDBCService jdbcService) {
        this.jdbcService = jdbcService; }

    @GetMapping("example1")
    public String example1() throws SQLException {
        return jdbcService.example1();
    }
    @GetMapping("example2")
    public String example2() throws SQLException {
        return jdbcService.example2();
    }
    @GetMapping("example3")
    public String example3() throws SQLException {
        return jdbcService.example3();
    }
    @GetMapping("example4")
    public String example4() throws SQLException {
        return jdbcService.example4();
    }
    @GetMapping("example5")
    public String example5() throws SQLException {
        return jdbcService.example5();
    }
    @GetMapping("example6")
    public String example6() throws SQLException {
        return jdbcService.example6();
    }
    @GetMapping("example7")
    public String example7() throws SQLException {
        return jdbcService.example7();
    }
}
