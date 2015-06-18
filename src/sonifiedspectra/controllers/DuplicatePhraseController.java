package sonifiedspectra.controllers;

import sonifiedspectra.model.HelpStrings;
import sonifiedspectra.model.Phrase;
import sonifiedspectra.view.PhraseInTrackView;
import sonifiedspectra.view.Sonify;
import sonifiedspectra.view.TrackView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Hvandenberg on 6/9/15.
 */
public class DuplicatePhraseController implements MouseListener {

    private Sonify app;

    public DuplicatePhraseController(Sonify app) {
        this.app = app;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        int multiplier = Integer.valueOf(app.getMovePitvTextField().getText());

        for (TrackView tv : app.getTrackViewArray()) {

            for (PhraseInTrackView pitv : tv.getPhraseInTrackViewArray()) {
                if (pitv.isSelected()) {

                    int x = pitv.getX() + pitv.getAdjustedWidth();
                    if (x < 0) x = 0;

                    double newTime = pitv.getPhrase().getBeatLength2() / 4;
                    double newX = pitv.getPhrase().getStartTime() + newTime;
                    if (newX < 0) newX = 0;

                    for (int i = 0; i < multiplier; i++) {
                        Phrase newPhrase = pitv.getPhrase().copy();
                        newPhrase.setId(app.getActiveProject().getCurrentPhraseId());
                        newPhrase.setStartTime(newX);
                        newTime = ((i + 2) * pitv.getPhrase().getBeatLength2()) / 4;
                        newX = pitv.getPhrase().getStartTime() + newTime;
                        app.getActiveProject().incrementPhraseId();
                        tv.getTrack().getPhrases().add(newPhrase);
                    }

                }
            }
            int i = 0;

                tv.initialize();
                for (PhraseInTrackView pitv2 : tv.getPhraseInTrackViewArray()) {
                   // pitv2.setBounds((int) ((pitv2.getPhrase().getStartTime() * 4) * 25), i * 15, pitv2.getAdjustedWidth(), 15);
                    pitv2.addMouseListener(new PhraseInTrackController(app, app.getActiveProject(), pitv2));
                    RemovePhraseFromTrackController removePhraseFromTrackController = new RemovePhraseFromTrackController(app, app.getActiveProject(), pitv2, tv);
                    pitv2.getRemoveButton().addMouseListener(new HelpTextController(app, HelpStrings.REMOVE_PHRASE_FROM_TRACK));
                    pitv2.getRemoveButton().addActionListener(removePhraseFromTrackController);
                    pitv2.getRemoveButton().addMouseListener(removePhraseFromTrackController);
                    System.out.println("Pitv " + pitv2.getPhrase().getId() + "-  Start time: " + pitv2.getPhrase().getStartTime() + ", End time: " + pitv2.getPhrase().getEndTime());
                    i++;
                }
                tv.repaint();

            /*for (PhraseInTrackView pitv : tv.getPhraseInTrackViewArray()) {
                if (pitv.isSelected()) {

                    double time = pitv.getPhrase().getEndTime();

                    for (int i = 0; i < multiplier; i++) {
                        Phrase newPhrase = app.getActivePhrase().copy();
                        newPhrase.setId(app.getActiveProject().getCurrentPhraseId());
                        newPhrase.setStartTime(time);
                        app.getActiveProject().incrementPhraseId();
                        tv.getTrack().getPhrases().add(newPhrase);
                        PhraseInTrackView newPITV = new PhraseInTrackView(pitv.getPhrase());
                        if (!newPhrase.isLoop()) newPITV.getNameLabel().setText(newPhrase.getCompound().getName());
                        else newPITV.getNameLabel().setText("Loop");
                        newPITV.setBounds((int) ((newPhrase.getStartTime() * 4) * 25), i * 15, newPITV.getAdjustedWidth(), 15);
                        tv.getPhraseInTrackViewArray().add(newPITV);
                        tv.add(newPITV);
                        newPITV.addMouseListener(new PhraseInTrackController(app, app.getActiveProject(), newPITV));
                        RemovePhraseFromTrackController removePhraseFromTrackController = new RemovePhraseFromTrackController(app, app.getActiveProject(), newPITV, tv);
                        newPITV.getRemoveButton().addMouseListener(new HelpTextController(app, HelpStrings.REMOVE_PHRASE_FROM_TRACK));
                        newPITV.getRemoveButton().addActionListener(removePhraseFromTrackController);
                        newPITV.getRemoveButton().addMouseListener(removePhraseFromTrackController);
                    }*/

            app.getSoundPlayer().reset();
            app.getSoundPlayer().updateSoundPlayer();

            app.getFrame().pack();

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        app.getDuplicatePhraseButton().setCol(app.getActivePhrase().getUnselectedColor());
        app.getDuplicatePhraseButton().repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        app.getDuplicatePhraseButton().setCol(app.getButtonBackgroundColor());
        app.getDuplicatePhraseButton().repaint();
    }
}
