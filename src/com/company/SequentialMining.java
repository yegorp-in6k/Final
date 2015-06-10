package com.company;

import com.prudsys.pdm.Core.CategoricalAttribute;
import com.prudsys.pdm.Core.Category;
import com.prudsys.pdm.Core.MiningModel;
import com.prudsys.pdm.Models.Sequential.ItemSetSeq;
import com.prudsys.pdm.Models.Sequential.RuleSetSeq;
import com.prudsys.pdm.Models.Sequential.SequentialMiningModel;
import com.prudsys.pdm.Models.Sequential.SequentialSettings;
com.prudsys.pdm.Models.Clustering.CDBased.Algorithms.KMeans.KMeans.
import java.io.FileReader;

/**
 * Created by Егор on 26.04.2015.
 */
public class SequentialMining {
    public SequentialMining()  throws Exception {

        // Read sequence rules from PMML file:
        MiningModel model = new SequentialMiningModel();
        FileReader reader = new FileReader("G:\\Xelopes6.5_openSource\\data\\pmml\\SequenceModel.xml");
        model.readPmml(reader);
        CategoricalAttribute categoryItemId = (CategoricalAttribute)
                ((SequentialSettings) model.getMiningSettings()).getItemId();
        System.out.println("-------------> PMML model read successfully");

        // Build recommendations:
        ((SequentialMiningModel) model).buildRecommendations();

        // Apply rules to new data vector ("0.Home", "1.2.Partner"):
        ItemSetSeq is = new ItemSetSeq();
        is.addItem( (int) categoryItemId.getKey( new Category("0.Home") ));
        is.addItem( (int) categoryItemId.getKey( new Category("1.2.Partner") ));
        RuleSetSeq rs = (RuleSetSeq) ((SequentialMiningModel) model).applyModel(is);
        if (rs != null) {
            System.out.println("nvec = " + rs.getSize());
            System.out.println("prem: " + rs.getPremise().toString());
            System.out.println("conc: " + rs.getConclusion().toString());
            System.out.println("Recommendation as string: ");
            for (int i = 0; i < rs.getPremise().getSize(); i++) {
                Category recItem = categoryItemId.getCategory(rs.getPremise().getItemAt(i));
                System.out.print(recItem.getDisplayValue());
                if (i < rs.getPremise().getSize()-1) System.out.print(", ");
            };
            System.out.print(" => ");
            for (int i = 0; i < rs.getConclusion().getSize(); i++) {
                Category recItem = categoryItemId.getCategory(rs.getConclusion().getItemAt(i));
                System.out.println(recItem.getDisplayValue());
                if (i < rs.getConclusion().getSize()-1) System.out.print(", ");
                else System.out.println();
            };
        }
        else
            System.out.println("No recommendations found.");
    }
}
