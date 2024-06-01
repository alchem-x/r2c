package alchem.r2c;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.nio.file.*;
import java.util.List;

@RequiredArgsConstructor
@Component
public class R2Command implements ApplicationRunner {

    private final R2Service r2Service;

    private abstract static class Commands {
        private static final String GET_OBJECT = "get-object";
        private static final String PUT_OBJECT = "put-object";
        private static final String DELETE_OBJECT = "delete-object";
    }

    @Override
    public void run(@NotNull ApplicationArguments args) {
        if (AppEnv.R2C.equals("CLI")) {
            var options = args.getNonOptionArgs();
            if (args.getOptionValues("help") != null) {
                Console.log("See: https://github.com/alchemy-works/r2c");
            } else if (options.isEmpty()) {
                Console.error("Error: command required");
                System.exit(-1);
            } else if (options.size() < 2) {
                Console.error("Error: object key required");
                System.exit(-1);
            } else {
                this.handleCommand(options);
            }
        }
    }

    public void handleCommand(@NotNull List<String> options) {
        var command = options.get(0);
        var objectKey = options.get(1);
        try {
            switch (command) {
                case Commands.GET_OBJECT -> this.getObject(objectKey);
                case Commands.PUT_OBJECT -> {
                    if (options.size() < 3) {
                        Console.error("Error: filepath required");
                        System.exit(-1);
                    } else {
                        var filepath = options.get(2);
                        this.putObject(objectKey, filepath);
                    }
                }
                case Commands.DELETE_OBJECT -> this.deleteObject(objectKey);
                default -> {
                    Console.error("Error: illegal command, see: https://github.com/alchemy-works/r2c");
                    System.exit(-1);
                }
            }
        } catch (Exception ex) {
            var cause = NestedExceptionUtils.getMostSpecificCause(ex);
            Console.error("Error: %s".formatted(cause.getMessage()));
            System.exit(-1);
        } finally {
            this.r2Service.closeClient();
        }
    }

    @SneakyThrows
    public void getObject(@NotNull String objectKey) {
        var object = this.r2Service.getObject(objectKey);
        var path = Paths.get(AppEnv.R2_DIST, object.object());
        Files.createDirectories(path.getParent());
        Files.copy(object, path, StandardCopyOption.REPLACE_EXISTING);
        Console.log("Get object %s".formatted(path));
    }

    @SneakyThrows
    public void putObject(@NotNull String objectKey, @NotNull String filepath) {
        var path = Paths.get(filepath);
        this.r2Service.putObject(new FileSystemResource(path), objectKey, Files.probeContentType(path));
        Console.log("Put object %s %s".formatted(objectKey, path));
    }

    public void deleteObject(@NotNull String objectKey) {
        this.r2Service.removeObject(objectKey);
        Console.log("Delete object %s".formatted(objectKey));
    }

}
