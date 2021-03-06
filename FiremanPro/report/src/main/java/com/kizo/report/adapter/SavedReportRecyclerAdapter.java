package com.kizo.report.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kizo.report.R;
import com.kizo.report.holder.SavedReportHolder;
import com.project.test.database.Entities.House_photos;
import com.project.test.database.Entities.Reports;
import com.project.test.database.Entities.report.Intervention_track;

import java.util.List;

/**
 *
 * Adapter za priakzivanje intervencija u obliku liste.
 *
 * Svaki element liste je jedna kartica koja sadrži sliku kuće na kojoj je bila inezrvencija, te nekoliko osnovnih podataka.
 *
 * <p>
 * Created by Zoran on 27.11.2017..
 * </p>
 * @author Zoran Hrnčić
 */

public class SavedReportRecyclerAdapter extends RecyclerView.Adapter<SavedReportHolder> {
    private LayoutInflater mInflator;

    List<Intervention_track> reports;

    public SavedReportRecyclerAdapter(List<Intervention_track> parentList) {
        this.reports = parentList;
    }

    @Override
    public SavedReportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View houseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_reports_list_item, parent, false);
        return new SavedReportHolder(houseView);
    }

    @Override
    public void onBindViewHolder(SavedReportHolder holder, int position) {
        holder.bind(reports.get(position), position);
    }



    @Override
    public int getItemCount() {
        return reports.size();
    }
}