package com.leopard4.contactapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.leopard4.contactapp.data.DatabaseHandler;
import com.leopard4.contactapp.model.Contact;

public class EditActivity extends AppCompatActivity {

    EditText editName;
    EditText editPhone;
    Button btnSave;

    int id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        btnSave = findViewById(R.id.btnSave);

//        아래 코드는 학예회 수준의 코드고
//        int id = getIntent().getIntExtra("id", 0);
//        String name = getIntent().getStringExtra("name");
//        String phone = getIntent().getStringExtra("phone");
//        editName.setText(name);
//        editPhone.setText(phone);

        // Contact 객체를 직렬화해서 보내면, 받는 쪽에서도 Contact 객체로 받을 수 있다.
        // 받은걸 다시 Contact 객체로 변환해서 사용하면 된다. 이것이 실무
        Contact contact = (Contact) getIntent().getSerializableExtra("contact");
        id = contact.getId();
        editName.setText(contact.getName());
        editPhone.setText(contact.getPhone());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editName.getText().toString().trim();
                String phone = editPhone.getText().toString().trim();

                // 이름과 전화번호가 모두 있어야 한다!
                if (name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(EditActivity.this, "이름과 전화번호를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 묶어서 처리할 Contact 클래스를 하나 만든다.
                Contact contact = new Contact(id, name, phone);

                // DB에 저장한다.
                DatabaseHandler db = new DatabaseHandler(EditActivity.this);
                db.updateContact(contact);

                // 유저한테 잘 저장되었다고, 알려주고
                Toast.makeText(EditActivity.this, "수정되었습니다", Toast.LENGTH_SHORT).show();

                // 액티비티 종료, 메인 액티비티로 돌아간다.(메인액티비티는 뒤에 숨어있었으므로)
                finish();
            }
        });
    }
}
