package s1546270.songle.Recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import s1546270.songle.R;

/**
 * Adapter for the recyclerview used to show the words found cards
 */

public class WordsFoundAdapter extends RecyclerView.Adapter<WordsFoundAdapter.RecyclerVH> {


    Context context;
    String[] wordsFound;

    public WordsFoundAdapter(Context context, String[] wordsFound) {
        this.context = context;
        this.wordsFound = wordsFound;
    }

    @Override
    public RecyclerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.word_found_card,parent,false);
        return new RecyclerVH(view);
    }

    @Override
    public void onBindViewHolder(RecyclerVH holder, int position) {
        holder.nameTxt.setText(wordsFound[position]);

    }

   @Override
   public int getItemCount() {
        return wordsFound.length;
   }


    public class RecyclerVH extends RecyclerView.ViewHolder {

        TextView nameTxt;

        public RecyclerVH(View itemView) {
            super(itemView);

            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
        }

    }


}
