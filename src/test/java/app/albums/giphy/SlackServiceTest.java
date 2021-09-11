package app.albums.giphy;

import org.junit.jupiter.api.Test;

public class SlackServiceTest extends BaseTest {

    @Test
    public void send_slash_event() {
        // given
        final String text = "welcome";

        // when
        createSlashEvent(text).build();

        // then
        methodsClientMock.popEphemeral(p -> true)
                .assertThat("arg$3[0].title.text", text);

    }
}
