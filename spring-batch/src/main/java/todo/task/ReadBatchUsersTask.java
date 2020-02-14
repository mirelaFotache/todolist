package todo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ReadBatchUsersTask implements Tasklet {

    private static Logger log = LoggerFactory.getLogger(ReadBatchUsersTask.class);

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("ReadBatchUsersTask start..");

        // ... your code

        log.info("ReadBatchUsersTask done..");
        return RepeatStatus.FINISHED;
    }
}
