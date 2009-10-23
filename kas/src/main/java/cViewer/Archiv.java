package cViewer;

/*
 * Copyright 2009 Erhard Kuenzel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayDeque;
import java.util.Deque;

public class Archiv {

    private final Deque<String> undoStack;

    private final Deque<String> redoStack;

    /**
     * Default Constructor.
     */
    public Archiv() {
        this.undoStack = new ArrayDeque<String>();
        this.redoStack = new ArrayDeque<String>();
    }

    public void addEdit(final String s) {
        this.undoStack.push(s);
        this.redoStack.clear();
    }

    public void discardAllEdits() {
        this.undoStack.clear();
        this.redoStack.clear();
    }

    public boolean canUndo() {
        return !this.undoStack.isEmpty();
    }

    public String undo(final String now) {
        if (this.undoStack.size() > 0) {
            this.redoStack.push(now);
            return this.undoStack.poll();
        } else {
            return "";
        }
    }

    public boolean canRedo() {
        return !this.redoStack.isEmpty();
    }

    public String redo(final String now) {
        if (this.redoStack.size() > 0) {
            this.undoStack.push(now);
            return this.redoStack.poll();
        } else {
            return "";
        }
    }
}
