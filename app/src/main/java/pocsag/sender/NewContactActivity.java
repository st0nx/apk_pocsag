package pocsag.sender;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        final EditText name = findViewById(R.id.nameText);
        final EditText number = findViewById(R.id.numberText);

        Button apply = findViewById(R.id.addContactNew);
        apply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent();
                myIntent.putExtra("name", name.getText().toString());
                myIntent.putExtra("number", number.getText().toString());
                setResult(RESULT_OK, myIntent);
                finish();
            }
        });
    }
}
