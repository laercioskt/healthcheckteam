package br.com.empresa.healthcheckteam.ui.assessments;

import br.com.empresa.healthcheckteam.backend.data.AssessmentQuestion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AssessmentIterator implements Iterator<AssessmentQuestion> {

    private final List<AssessmentQuestion> list;
    private int indexSelected = -1;

    public AssessmentIterator(Set<AssessmentQuestion> list) {
        this.list = new ArrayList<>(list);
    }

    @Override
    public boolean hasNext() {
        return indexSelected < list.size() - 1;
    }

    public boolean hasPrevious() {
        return indexSelected > 0;
    }

    @Override
    public AssessmentQuestion next() {
        indexSelected++;
        return current();
    }

    @Override
    public void remove() {
        list.remove(indexSelected);
    }

    public void remove(int i) {
        list.remove(i);
        if (i < indexSelected) {
            indexSelected--;
        }
    }

    public AssessmentQuestion previous() {
        indexSelected--;
        return current();
    }

    public AssessmentQuestion current() {
        return list.get(indexSelected);
    }

    public AssessmentQuestion get(int i) {
        return list.get(i);
    }

}
