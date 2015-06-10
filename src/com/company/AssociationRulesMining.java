package com.company;

import com.prudsys.pdm.Core.CategoricalAttribute;
import com.prudsys.pdm.Core.Category;
import com.prudsys.pdm.Core.MiningModel;
import com.prudsys.pdm.Models.AssociationRules.AssociationRulesMiningModel;
import com.prudsys.pdm.Models.AssociationRules.AssociationRulesSettings;
import com.prudsys.pdm.Models.AssociationRules.ItemSet;
import com.prudsys.pdm.Models.AssociationRules.RuleSet;

import java.io.FileReader;

/**
 * Created by Егор on 26.04.2015.
 */
public class AssociationRulesMining {

    public AssociationRulesMining() throws Exception {

        // Read association rules from PMML file:
        MiningModel model = new AssociationRulesMiningModel();
        FileReader reader = new FileReader("G:\\Xelopes6.5_openSource\\data\\pmml\\AssociationRulesModel.xml");
        model.readPmml(reader);
        CategoricalAttribute categoryItemId = (CategoricalAttribute)
                ((AssociationRulesSettings) model.getMiningSettings()).getItemId();
        System.out.println("-------------> PMML model read successfully");

        // Build recommendations:
        ((AssociationRulesMiningModel) model).buildRecommendations();

        // Apply rules to new data vector (2, 5):
        ItemSet is = new ItemSet();
        is.addItem( 2 );   // direct key, not recommended
        is.addItem( (int) categoryItemId.getKey( new Category("5") ));  // correct input
        RuleSet rs = (RuleSet) ((AssociationRulesMiningModel) model).applyModel(is);
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
