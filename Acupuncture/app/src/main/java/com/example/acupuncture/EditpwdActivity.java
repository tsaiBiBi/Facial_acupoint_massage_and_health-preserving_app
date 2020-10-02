package com.example.acupuncture;
import androidx.appcompat.app.AppCompatActivity;

// widget
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// webservice
import com.example.webservice.User;

public class EditpwdActivity extends AppCompatActivity {

    EditText et_old_pwd , et_new_pwd , et_confirm_pwd;
    Button btn_edit_pwd_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpwd);

        // edittext
        et_old_pwd = (EditText) findViewById(R.id.eT_pwd_old);
        et_new_pwd = (EditText) findViewById(R.id.eT_pwd_new);
        et_confirm_pwd = (EditText) findViewById(R.id.eT_pwd_confirm);

        // button
        btn_edit_pwd_ok = (Button) findViewById(R.id.btn_edit_pwd_ok);

        // 判斷空值及註冊
        btn_edit_pwd_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fold_pwd     = et_old_pwd.getText().toString().trim();
                String fnew_pwd     = et_new_pwd.getText().toString().trim();
                String fconfirm_pwd = et_confirm_pwd.getText().toString().trim();
                if(!chk_null(fold_pwd , fnew_pwd , fconfirm_pwd)) {
                    if(fnew_pwd.equals(fconfirm_pwd))
                        User.update_pwd(EditpwdActivity.this , fold_pwd , fnew_pwd);
                    else {
                        Toast.makeText(EditpwdActivity.this ,"請確認密碼輸入是否正確" , Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean chk_null(String fnew_pwd , String fold_pwd, String fconfirm_pwd) {
        Boolean isnull = false;
        if(fnew_pwd .isEmpty() || fold_pwd.isEmpty() || fconfirm_pwd.isEmpty()) {
            Toast.makeText(EditpwdActivity.this ,"請填完所有表格" , Toast.LENGTH_LONG).show();
            isnull = true;
        }
        return isnull;
    }
}