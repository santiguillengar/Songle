package s1546270.songle.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import s1546270.songle.DrawerActivity;
import s1546270.songle.R;
import s1546270.songle.Recycler.MyAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Following this tutorial: https://www.youtube.com/watch?v=CUWUBVdbDUA
 */
public class WordsFound extends Fragment {


    private RecyclerView rv;
    private static String[] wordsFound;

    public WordsFound() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        wordsFound = makeWordsFoundArray();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_words_found, container, false);

        rv = (RecyclerView) view.findViewById(R.id.wordsFound_recyclerView);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv.setAdapter(new MyAdapter(getActivity(), wordsFound));

        return view;
    }

    public static WordsFound newInstance() {
        WordsFound wordsFound = new WordsFound();


        return wordsFound;
    }

    public String[] makeWordsFoundArray() {
        List<String> wordsFound = ((DrawerActivity) getActivity()).getWordsFound();
        return wordsFound.toArray(new String[0]);
    }

    @Override
    public String toString() {
        return "wordsFound";
    }

}
