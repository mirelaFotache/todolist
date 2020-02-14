package todo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ModifyBatchUsersTask implements Tasklet {

    private static Logger log = LoggerFactory.getLogger(ModifyBatchUsersTask.class);

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("ModifyBatchUsersTask start..");

        // ... your code

        log.info("ModifyBatchUsersTask done..");
        return RepeatStatus.FINISHED;
    }
}
