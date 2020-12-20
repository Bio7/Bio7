/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sourceforge.texlipse.bibparser.node;

import java.util.*;
import net.sourceforge.texlipse.bibparser.analysis.*;

@SuppressWarnings("nls")
public final class ABibtex extends PBibtex
{
    private final LinkedList<PBibEntry> _bibEntry_ = new LinkedList<PBibEntry>();

    public ABibtex()
    {
        // Constructor
    }

    public ABibtex(
        @SuppressWarnings("hiding") List<PBibEntry> _bibEntry_)
    {
        // Constructor
        setBibEntry(_bibEntry_);

    }

    @Override
    public Object clone()
    {
        return new ABibtex(
            cloneList(this._bibEntry_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseABibtex(this);
    }

    public LinkedList<PBibEntry> getBibEntry()
    {
        return this._bibEntry_;
    }

    public void setBibEntry(List<PBibEntry> list)
    {
        this._bibEntry_.clear();
        this._bibEntry_.addAll(list);
        for(PBibEntry e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._bibEntry_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._bibEntry_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        for(ListIterator<PBibEntry> i = this._bibEntry_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PBibEntry) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        throw new RuntimeException("Not a child.");
    }
}
