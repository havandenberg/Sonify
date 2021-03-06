package sonifiedspectra.controllers;

import sonifiedspectra.model.Compound;
import sonifiedspectra.model.HelpStrings;
import sonifiedspectra.model.Phrase;
import sonifiedspectra.model.Project;
import sonifiedspectra.view.PhraseView;
import sonifiedspectra.view.Sonify;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Hvandenberg on 5/31/15.
 */
public class AddPhraseController implements ActionListener, MouseListener {

    private Sonify app;
    private Project project;

    public AddPhraseController(Sonify app, Project project) {
        this.project = project;
        this.app = app;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Compound compound = null;

        for (Compound c : project.getCompoundsArray()) {

            if (String.valueOf(app.getCompoundComboBox().getSelectedItem()).equals(c.getName())) {
                compound = c;
            }

        }

        if (compound != null) System.out.println( compound.getName() + " found.");
        else System.out.println("Could not find active compound.");

        Phrase newPhrase = new Phrase(app.getActiveProject().getCurrentPhraseId(), compound, app.getColorsArray().get(app.getCurrentColorIndex()), 2500, 1000);
        app.incrementColorIndex();
        app.getActiveProject().incrementPhraseId();
        newPhrase.initialize();

        PhraseView phraseView = new PhraseView(newPhrase);
        phraseView.setBackground(phraseView.getPhrase().getUnselectedColor());
        phraseView.updatePanel();
        phraseView.setBounds(10 + 130 * (app.getPhraseViewArray().size()), 5, 120, 70);
        phraseView.addMouseListener(new HelpTextController(app, HelpStrings.PHRASE_VIEW));
        phraseView.addMouseListener(new PhraseController(app.getActiveProject(), app, phraseView));

        app.getPhraseViewArray().add(phraseView);
        app.getPhrasesPanel().add(phraseView);
        app.getActiveProject().getPhrasesArray().add(newPhrase);
        app.getPhrasesPanel().setPreferredSize(new Dimension(10 + 130 * app.getActiveProject().getPhrasesArray().size(), 100));
        app.updateActivePhrase(newPhrase);
        app.updateIntervalMarker();
        app.getSoundPlayer().reset();
        app.getSoundPlayer().updateSoundPlayer();
        app.getFrame().pack();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        app.getAddPhraseButton().setCol(app.getActivePhrase().getSelectedColor());
        app.getAddPhraseButton().repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        app.getAddPhraseButton().setCol(app.getActivePhrase().getUnselectedColor());
        app.getAddPhraseButton().repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        app.getAddPhraseButton().setCol(app.getActivePhrase().getUnselectedColor());
        app.getAddPhraseButton().repaint();
        app.getFrame().pack();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        app.getAddPhraseButton().setCol(app.getButtonBackgroundColor());
        app.getAddPhraseButton().repaint();
        app.getFrame().pack();
    }
}
