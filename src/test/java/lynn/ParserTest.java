package lynn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ParserTest {
    @Test
    void parseTask_validDeadline_createsFormattedDeadline() throws LynnException {
        Task task = Parser.parseTask("deadline submit report /by 2026-10-15");

        assertEquals("[D][ ] submit report (by: Oct 15 2026)", task.toString());
    }

    @Test
    void parseTask_invalidDeadlineDate_throwsHelpfulException() {
        LynnException exception = assertThrows(LynnException.class,
                () -> Parser.parseTask("deadline submit report /by tomorrow"));

        assertEquals("Use yyyy-MM-dd for deadline dates, for example 2026-09-01.", exception.getMessage());
    }
}
