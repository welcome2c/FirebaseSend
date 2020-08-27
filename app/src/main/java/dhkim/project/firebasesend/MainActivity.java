package dhkim.project.firebasesend;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etInputMessage;
    private Button btnSend;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInputMessage = findViewById(R.id.etInputMessage);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etInputMessage.length() != 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FireBase fireBase = null;
                            try {
                                fireBase = new FireBase("title", etInputMessage.getText().toString());
                                fireBase.sendToTopicMessage("your_topic");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }
}
