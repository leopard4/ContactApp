package com.leopard4.contactapp.adapter;

// 1. RecyclerView.Adapter 를 상속받는다.

// 2. 상속받은 클래스가 abstract 이므로, unimplemented method 오버라이드!

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.leopard4.contactapp.EditActivity;
import com.leopard4.contactapp.R;
import com.leopard4.contactapp.data.DatabaseHandler;
import com.leopard4.contactapp.model.Contact;

import java.util.List;


// 6. RecyclerView.Adapter 의 데이터 타입(<ContactAdapter.viewHolder>)을 적어주어야 한다.
//    우리가 만든 ViewHolder 로 적는다.

// 7. 빨간색 에러가 뜨면, 우리가만든 ViewHolder 로,
//    onCreateViewHolder, onBindViewHolder 함수도 변경해준다.
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.viewHolder> {

    // 5. 어댑터 클래스의 멤버변수와 생성자를 만들어 준다.
    Context context; // 어떤 액티비티에서 실행할건데 라는것
    List<Contact> contactList;

    // 유저가 삭제를 누른 인덱스
    int deleteIndex;

    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    // 8. 오버라이드 함수 3개를 전부 구현해 주면 끝!
    @NonNull
    @Override
    public ContactAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml 파일을 연결하는 작업
        // 재사용 가능한 코드이므로 리턴클래스명, 파일명만 바꿔주자. (contact_row)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);
        return new ContactAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.viewHolder holder, int position) {
        // 뷰에 데이터를 셋팅한다!
        // 파라미터 holder = 행 하나하나를 의미한다. position = 몇번째 행인지를 의미한다.
        Contact contact = contactList.get(position);

        holder.txtName.setText(contact.name);
        holder.txtPhone.setText(contact.phone);

    }

    @Override
    public int getItemCount() {
        // 전체 데이터의 갯수를 적어준다.
        return contactList.size();
    }

    // 3. ViewHolder 클래스를 만든다.
    //    이 클래스는 row.xml 화면에 있는 뷰를 연결시키는 클래스다.
    //    ViewHolder 클래스는 RecyclerView.ViewHolder 를 상속받는다.

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPhone;
        ImageView imgDelete;
        CardView cardView;

        // 4. 생성자를 만든다.
        //    생성자에서, 뷰를 연결시키는 코드를 작성하고,
        //    클릭 이벤트도 작성한다.
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView = itemView.findViewById(R.id.cardView);

            // 9. 클릭 이벤트를 만들어 준다.
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 1) 인텐트에 유저가 어떤 행을 눌렀는지 파악하여
                    //    누른 이름과 전화번호를 담아서
                    int index = getAdapterPosition(); // 어떤 행을 눌렀는지 인덱스로 알려준다.
                    Contact contact = contactList.get(index); // 해당 행의 데이터를 가져온다.

                    // 2) 수정 액티비티를 띄운다.
                    //    어떤 액티비티가 어떤 액티비티를 띄운다!! => 인텐트에 있어야 한다.
                    Intent intent = new Intent(context, EditActivity.class);

//                    intent.putExtra("id", contact.id);
//                    intent.putExtra("name", contact.name);
//                    intent.putExtra("phone", contact.phone);

                    // 위와같이 하면 귀찮으니까 Contact를 직렬화해서 한번에 넘겨준다
                    // 직렬화는 Contact 클래스에 Serializable을 implements 해줘야 한다.
                    intent.putExtra("contact", contact);

                    context.startActivity(intent);

                }
            });

            // 10. 삭제 이벤트를 만들어 준다.
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 0) 어느 주소록을 삭제할것인지
                    //    삭제할 주소록을 가져온다 (어떤 행을 눌렀는지 파악한다.)
                    deleteIndex = getAdapterPosition();

                    // 1) 알러트 다이얼로그 나온다.
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("삭제");
                    builder.setMessage("정말 삭제하시겠습니까?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 2) 알러트 다이얼로그에서 Yes 눌렀을때
                            //    데이터베이스에서 삭제
                            DatabaseHandler db = new DatabaseHandler(context);

                            Contact contact = contactList.get(deleteIndex);

                            db.deleteContact(contact);

                            // 알러트 다이얼로그는 액티비티가 아니므로
                            // 메인액티비티의 onResume 함수가 실행안된다.
                            // 따라서 화면 갱신이 안된다.

                            // 즉, 디비에 저장된 데이터 삭제했으니,
                            // 메모리에 저장된 데이터도 삭제한다.
                            contactList.remove(deleteIndex);
                            // 데이터가 변경되었으니, 화면 갱신하라고 어댑터의 함수호출 (상속받은기능)
                            notifyDataSetChanged();
                        }
                    });
                    // 3) 알러트 다이얼로그에서 No 눌렀을때
                    //    아무것도 안한다.
                    builder.setNegativeButton("No", null);
                    builder.show();
                }


            });
        }
    }
}



