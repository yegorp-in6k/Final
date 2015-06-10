package com.company;

import com.prudsys.pdm.Core.CategoricalAttribute;
import com.prudsys.pdm.Core.Category;
import com.prudsys.pdm.Core.MiningModel;
import com.prudsys.pdm.Models.AssociationRules.ItemSet;
import com.prudsys.pdm.Models.CustomerSeq.CustomRuleSetSeq;
import com.prudsys.pdm.Models.CustomerSeq.CustomSequence;
import com.prudsys.pdm.Models.CustomerSeq.CustomerSequentialMiningModel;
import com.prudsys.pdm.Models.CustomerSeq.CustomerSequentialSettings;

import java.io.FileReader;

/**
 * Created by Егор on 26.04.2015.
 */
public class CustomerSequentialMining {
    public CustomerSequentialMining() throws Exception{
        // Read sequence rules from PMML file:
        MiningModel model = new CustomerSequentialMiningModel();
        FileReader reader = new FileReader("G:\\Xelopes6.5_openSource\\data\\pmml\\CustomerSequentialModel.xml");
        model.readPmml(reader);
        CategoricalAttribute categoryItemId = (CategoricalAttribute)
                ((CustomerSequentialSettings) model.getMiningSettings()).getItemId();
        System.out.println("-------------> PMML model read successfully");

        // Build recommendations:
        ((CustomerSequentialMiningModel) model).buildRecommendations();

        // Apply rules to trivial 'customer sequence' ("item_2", "item_3", "item_5"):
        ItemSet is = new ItemSet();
        is.addItem( (int) categoryItemId.getKey( new Category("item_2")) );
        is.addItem( (int) categoryItemId.getKey( new Category("item_3") ));
        is.addItem( (int) categoryItemId.getKey( new Category("item_5") ));
        CustomSequence cs = new CustomSequence();
        cs.addItemSet(is);
        CustomRuleSetSeq crs = (CustomRuleSetSeq) ((CustomerSequentialMiningModel) model).applyModel(cs);
        if (crs != null) {
            System.out.println("ncseqs = " + crs.getSize());
            System.out.println("prem: " + crs.getPremise().toString());
            System.out.println("conc: " + crs.getConclusion().toString());
            System.out.println("Recommendation as string: ");
            for (int i = 0; i < crs.getPremise().getSize(); i++) {
                ItemSet iset = crs.getPremise().getItemSet(i);
                for (int j = 0; j < iset.getSize(); j++) {
                    Category recItem = categoryItemId.getCategory(iset.getItemAt(j));
                    System.out.print( recItem.getDisplayValue() );
                    if (j < iset.getSize() - 1)
                        System.out.print(", ");
                };
                if (i < crs.getPremise().getSize() - 1)
                    System.out.print("| ");
            };
            System.out.print(" => ");
            for (int i = 0; i < crs.getConclusion().getSize(); i++) {
                ItemSet iset = crs.getConclusion().getItemSet(i);
                for (int j = 0; j < iset.getSize(); j++) {
                    Category recItem = categoryItemId.getCategory( iset.getItemAt(j) );
                    System.out.print( recItem.getDisplayValue() );
                    if (j < iset.getSize() - 1)
                        System.out.print(", ");
                };
                if (i < crs.getConclusion().getSize() - 1)
                    System.out.print("| ");
                else
                    System.out.println();
            };
        }
        else
            System.out.println("No recommendations found.");
    }
}
