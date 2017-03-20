/*
 * Copyright (c) 2002-2016, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package org.jline.reader;

import java.io.InputStream;
import java.util.Map;

import org.jline.keymap.KeyMap;
import org.jline.reader.impl.BufferImpl;
import org.jline.terminal.Terminal;

/** Read lines from the console, with input editing.
 *
 * <h3>Prompt strings</h3>
 * <p>
 * It is traditional for an interactive console-based program
 * to print a short prompt string to signal that the user is expected
 * to type a command.  JLine supports 3 kinds of prompt string:
 * <ul>
 * <li> The normal prompt at the start (left) of the initial line of a command.
 * <li> An optional right prompt at the right border of the initial line.
 * <li> A start (left) prompt for continuation lines.  I.e. the lines
 * after the first line of a multi-line command.
 * </ul>
 * <p>
 * All of these are specified with prompt templates,
 * which are similar to {@code printf} format strings,
 * using the character {@code '%'} to indicate special functionality.
 * <p>
 * The pattern may include ANSI escapes.
 * It may include these template markers:
 * <dl>
 * <dt>{@code %N}</dt>
 * <dd>A line number. This is the sum of {@code getLineNumber()}
 *   and a counter starting with 1 for the first continuation line.
 * <dt>{@code %M}</dt>
 * <dd>A short word explaining what is "missing". This is supplied from
 * the {@link EOFError#getMissing()} method, if provided.
 * Defaults to an empty string.
 * </dd>
 * <dt>{@code %}<var>n</var>{@code P}<var>c</var></dt>
 * <dd>Insert padding at this possion, repeating the following
 *   character <var>c</var> as needed to bring the total prompt
 *   column width as specified by the digits <var>n</var>.
 * </dd>
 * <dt>{@code %P}<var>c</var></dt>
 * <dd>As before, but use width from the initial prompt.
 * </dd>
 * <dt>{@code %%}</dt>
 * <dd>A literal {@code '%'}.
 * </dd>
 * <dt><code>%{</code></dt>
 * <dt><code>%}</code></dt>
 * Text between a <code>%{</code>...<code>%}</code> pair is printed as
 * part of a prompt, but not interpreted by JLine
 * (except that {@code '%'}-escapes are processed).  The text is assumed
 * to take zero columns (not move the cursor).  If it changes the style,
 * you're responsible for changing it back.  Standard ANSI escape sequences
 * do not need to be within a <code>%{</code>...<code>%}</code> pair
 * (though can be) since JLine knows how to deal with them.  However,
 * these delimiters are needed for unusual non-standard escape sequences.
 * <dd>
 * </dd>
 * </dl>
 */

public interface LineReader {

    //
    // Widget names
    //
    String CALLBACK_INIT = "callback-init";
    String CALLBACK_FINISH = "callback-finish";
    String CALLBACK_KEYMAP = "callback-keymap";

    String ACCEPT_LINE = "accept-line";
    String ARGUMENT_BASE = "argument-base";
    String BACKWARD_CHAR = "backward-char";
    String BACKWARD_DELETE_CHAR = "backward-delete-char";
    String BACKWARD_DELETE_WORD = "backward-delete-word";
    String BACKWARD_KILL_LINE = "backward-kill-line";
    String BACKWARD_KILL_WORD = "backward-kill-word";
    String BACKWARD_WORD = "backward-word";
    String BEEP = "beep";
    String BEGINNING_OF_BUFFER_OR_HISTORY = "beginning-of-buffer-or-history";
    String BEGINNING_OF_HISTORY = "beginning-of-history";
    String BEGINNING_OF_LINE = "beginning-of-line";
    String BEGINNING_OF_LINE_HIST = "beginning-of-line-hist";
    String CAPITALIZE_WORD = "capitalize-word";
    String CHARACTER_SEARCH = "character-search";
    String CHARACTER_SEARCH_BACKWARD = "character-search-backward";
    String CLEAR_SCREEN = "clear-screen";
    String COMPLETE_PREFIX = "complete-prefix";
    String COMPLETE_WORD = "complete-word";
    String COPY_PREV_WORD = "copy-prev-word";
    String COPY_REGION_AS_KILL = "copy-region-as-kill";
    String DELETE_CHAR = "delete-char";
    String DELETE_CHAR_OR_LIST = "delete-char-or-list";
    String DELETE_WORD = "delete-word";
    String DIGIT_ARGUMENT = "digit-argument";
    String DO_LOWERCASE_VERSION = "do-lowercase-version";
    String DOWN_CASE_WORD = "down-case-word";
    String DOWN_HISTORY = "down-history";
    String DOWN_LINE = "down-line";
    String DOWN_LINE_OR_HISTORY = "down-line-or-history";
    String DOWN_LINE_OR_SEARCH = "down-line-or-search";
    String EMACS_BACKWARD_WORD = "emacs-backward-word";
    String EMACS_EDITING_MODE = "emacs-editing-mode";
    String EMACS_FORWARD_WORD = "emacs-forward-word";
    String END_OF_BUFFER_OR_HISTORY = "end-of-buffer-or-history";
    String END_OF_HISTORY = "end-of-history";
    String END_OF_LINE = "end-of-line";
    String END_OF_LINE_HIST = "end-of-line-hist";
    String EXCHANGE_POINT_AND_MARK = "exchange-point-and-mark";
    String EXECUTE_NAMED_CMD = "execute-named-cmd";
    String EXPAND_HISTORY = "expand-history";
    String EXPAND_OR_COMPLETE = "expand-or-complete";
    String EXPAND_OR_COMPLETE_PREFIX = "expand-or-complete-prefix";
    String EXPAND_WORD = "expand-word";
    String FORWARD_CHAR = "forward-char";
    String FORWARD_WORD = "forward-word";
    String HISTORY_BEGINNING_SEARCH_BACKWARD = "history-beginning-search-backward";
    String HISTORY_BEGINNING_SEARCH_FORWARD = "history-beginning-search-forward";
    String HISTORY_INCREMENTAL_PATTERN_SEARCH_BACKWARD = "history-incremental-pattern-search-backward";
    String HISTORY_INCREMENTAL_PATTERN_SEARCH_FORWARD = "history-incremental-pattern-search-forward";
    String HISTORY_INCREMENTAL_SEARCH_BACKWARD = "history-incremental-search-backward";
    String HISTORY_INCREMENTAL_SEARCH_FORWARD = "history-incremental-search-forward";
    String HISTORY_SEARCH_BACKWARD = "history-search-backward";
    String HISTORY_SEARCH_FORWARD = "history-search-forward";
    String INSERT_CLOSE_CURLY = "insert-close-curly";
    String INSERT_CLOSE_PAREN = "insert-close-paren";
    String INSERT_CLOSE_SQUARE = "insert-close-square";
    String INFER_NEXT_HISTORY = "infer-next-history";
    String INSERT_COMMENT = "insert-comment";
    String INSERT_LAST_WORD = "insert-last-word";
    String KILL_BUFFER = "kill-buffer";
    String KILL_LINE = "kill-line";
    String KILL_REGION = "kill-region";
    String KILL_WHOLE_LINE = "kill-whole-line";
    String KILL_WORD = "kill-word";
    String LIST_CHOICES = "list-choices";
    String LIST_EXPAND = "list-expand";
    String MAGIC_SPACE = "magic-space";
    String MENU_EXPAND_OR_COMPLETE = "menu-expand-or-complete";
    String MENU_COMPLETE = "menu-complete";
    String MENU_SELECT = "menu-select";
    String NEG_ARGUMENT = "neg-argument";
    String OVERWRITE_MODE = "overwrite-mode";
    String PUT_REPLACE_SELECTION = "put-replace-selection";
    String QUOTED_INSERT = "quoted-insert";
    String READ_COMMAND = "read-command";
    String RECURSIVE_EDIT = "recursive-edit";
    String REDISPLAY = "redisplay";
    String REDRAW_LINE = "redraw-line";
    String REDO = "redo";
    String REVERSE_MENU_COMPLETE = "reverse-menu-complete";
    String SELF_INSERT = "self-insert";
    String SELF_INSERT_UNMETA = "self-insert-unmeta";
    String SEND_BREAK = "abort";
    String SET_LOCAL_HISTORY = "set-local-history";
    String SET_MARK_COMMAND = "set-mark-command";
    String SPELL_WORD = "spell-word";
    String SPLIT_UNDO = "split-undo";
    String TRANSPOSE_CHARS = "transpose-chars";
    String TRANSPOSE_WORDS = "transpose-words";
    String UNDEFINED_KEY = "undefined-key";
    String UNDO = "undo";
    String UNIVERSAL_ARGUMENT = "universal-argument";
    String UP_CASE_WORD = "up-case-word";
    String UP_HISTORY = "up-history";
    String UP_LINE = "up-line";
    String UP_LINE_OR_HISTORY = "up-line-or-history";
    String UP_LINE_OR_SEARCH = "up-line-or-search";
    String VI_ADD_EOL = "vi-add-eol";
    String VI_ADD_NEXT = "vi-add-next";
    String VI_BACKWARD_BLANK_WORD = "vi-backward-blank-word";
    String VI_BACKWARD_BLANK_WORD_END = "vi-backward-blank-word-end";
    String VI_BACKWARD_CHAR = "vi-backward-char";
    String VI_BACKWARD_DELETE_CHAR = "vi-backward-delete-char";
    String VI_BACKWARD_KILL_WORD = "vi-backward-kill-word";
    String VI_BACKWARD_WORD = "vi-backward-word";
    String VI_BACKWARD_WORD_END = "vi-backward-word-end";
    String VI_BEGINNING_OF_LINE = "vi-beginning-of-line";
    String VI_CHANGE = "vi-change-to";
    String VI_CHANGE_EOL = "vi-change-eol";
    String VI_CHANGE_WHOLE_LINE = "vi-change-whole-line";
    String VI_CMD_MODE = "vi-cmd-mode";
    String VI_DELETE = "vi-delete";
    String VI_DELETE_CHAR = "vi-delete-char";
    String VI_DIGIT_OR_BEGINNING_OF_LINE = "vi-digit-or-beginning-of-line";
    String VI_DOWN_LINE_OR_HISTORY = "vi-down-line-or-history";
    String VI_END_OF_LINE = "vi-end-of-line";
    String VI_FETCH_HISTORY = "vi-fetch-history";
    String VI_FIND_NEXT_CHAR = "vi-find-next-char";
    String VI_FIND_NEXT_CHAR_SKIP = "vi-find-next-char-skip";
    String VI_FIND_PREV_CHAR = "vi-find-prev-char";
    String VI_FIND_PREV_CHAR_SKIP = "vi-find-prev-char-skip";
    String VI_FIRST_NON_BLANK = "vi-first-non-blank";
    String VI_FORWARD_BLANK_WORD = "vi-forward-blank-word";
    String VI_FORWARD_BLANK_WORD_END = "vi-forward-blank-word-end";
    String VI_FORWARD_CHAR = "vi-forward-char";
    String VI_FORWARD_WORD = "vi-forward-word";
    String VI_FORWARD_WORD_END = "vi-forward-word-end";
    String VI_GOTO_COLUMN = "vi-goto-column";
    String VI_HISTORY_SEARCH_BACKWARD = "vi-history-search-backward";
    String VI_HISTORY_SEARCH_FORWARD = "vi-history-search-forward";
    String VI_INSERT = "vi-insert";
    String VI_INSERT_BOL = "vi-insert-bol";
    String VI_INSERT_COMMENT = "vi-insert-comment";
    String VI_JOIN = "vi-join";
    String VI_KILL_EOL = "vi-kill-eol";
    String VI_KILL_LINE = "vi-kill-line";
    String VI_MATCH_BRACKET = "vi-match-bracket";
    String VI_OPEN_LINE_ABOVE = "vi-open-line-above";
    String VI_OPEN_LINE_BELOW = "vi-open-line-below";
    String VI_OPER_SWAP_CASE = "vi-oper-swap-case";
    String VI_PUT_AFTER = "vi-put-after";
    String VI_PUT_BEFORE = "vi-put-before";
    String VI_QUOTED_INSERT = "vi-quoted-insert";
    String VI_REPEAT_CHANGE = "vi-repeat-change";
    String VI_REPEAT_FIND = "vi-repeat-find";
    String VI_REPEAT_SEARCH = "vi-repeat-search";
    String VI_REPLACE = "vi-replace";
    String VI_REPLACE_CHARS = "vi-replace-chars";
    String VI_REV_REPEAT_FIND = "vi-rev-repeat-find";
    String VI_REV_REPEAT_SEARCH = "vi-rev-repeat-search";
    String VI_SET_BUFFER = "vi-set-buffer";
    String VI_SUBSTITUTE = "vi-substitute";
    String VI_SWAP_CASE = "vi-swap-case";
    String VI_UNDO_CHANGE = "vi-undo-change";
    String VI_UP_LINE_OR_HISTORY = "vi-up-line-or-history";
    String VI_YANK = "vi-yank";
    String VI_YANK_EOL = "vi-yank-eol";
    String VI_YANK_WHOLE_LINE = "vi-yank-whole-line";
    String VISUAL_LINE_MODE = "visual-line-mode";
    String VISUAL_MODE = "visual-mode";
    String WHAT_CURSOR_POSITION = "what-cursor-position";
    String YANK = "yank";
    String YANK_POP = "yank-pop";
    String MOUSE = "mouse";

    //
    // KeyMap names
    //

    String VICMD = "vicmd";
    String VIINS = "viins";
    String VIOPP = "viopp";
    String VISUAL = "visual";
    String MAIN = "main";
    String EMACS = "emacs";
    String SAFE = ".safe";
    String MENU = "menu";

    //
    // Variable names
    //

    String BIND_TTY_SPECIAL_CHARS = "bind-tty-special-chars";
    String COMMENT_BEGIN = "comment-begin";
    String BELL_STYLE = "bell-style";
    String PREFER_VISIBLE_BELL = "prefer-visible-bell";
    String LIST_MAX = "list-max";
    String DISABLE_HISTORY = "disable-history";
    String DISABLE_COMPLETION = "disable-completion";
    String EDITING_MODE = "editing-mode";
    String KEYMAP = "keymap";
    String BLINK_MATCHING_PAREN = "blink-matching-paren";
    String WORDCHARS = "WORDCHARS";
    String REMOVE_SUFFIX_CHARS = "REMOVE_SUFFIX_CHARS";
    String SEARCH_TERMINATORS = "search-terminators";
    String ERRORS = "errors";
    /**
     * Set the template for prompts for secondary (continuation) lines.
     * This is a prompt template as described in the class header.
     */
    String SECONDARY_PROMPT_PATTERN = "secondary-prompt-pattern";
    /**
     * When in multiline edit mode, this variable can be used
     * to offset the line number displayed.
     */
    String LINE_OFFSET = "line-offset";

    /**
     * Timeout for ambiguous key sequences.
     * If the key sequence is ambiguous, i.e. there is a matching
     * sequence but the sequence is also a prefix for other bindings,
     * the next key press will be waited for a specified amount of
     * time.  If the timeout elapses, the matched sequence will be
     * used.
     */
    String AMBIGUOUS_BINDING = "ambiguous-binding";

    /**
     * Columns separated list of patterns that will not be saved in history.
     */
    String HISTORY_IGNORE = "history-ignore";

    /**
     * File system history path.
     */
    String HISTORY_FILE = "history-file";

    /**
     * Number of history items to keep in memory.
     */
    String HISTORY_SIZE = "history-size";

    /**
     * Number of history items to keep in the history file.
     */
    String HISTORY_FILE_SIZE = "history-file-size";

    Map<String, KeyMap<Binding>> defaultKeyMaps();

    enum Option {
        COMPLETE_IN_WORD,
        DISABLE_EVENT_EXPANSION,
        HISTORY_VERIFY,
        HISTORY_IGNORE_SPACE(true),
        HISTORY_IGNORE_DUPS(true),
        HISTORY_REDUCE_BLANKS(true),
        HISTORY_BEEP(true),
        HISTORY_INCREMENTAL(true),
        AUTO_MENU(true),
        AUTO_LIST(true),
        RECOGNIZE_EXACT,
        GROUP,
        CASE_INSENSITIVE,
        LIST_AMBIGUOUS,
        LIST_PACKED,
        LIST_ROWS_FIRST,
        GLOB_COMPLETE,
        MENU_COMPLETE,
        /** if set and not at start of line before prompt, move to new line */
        AUTO_FRESH_LINE,

        /** After writing into the rightmost column, do we immediately
         * move to the next line (the default)? Or do we wait until
         * the next character.
         * If set, an input line that is exactly {@code N*columns} wide will
         * use {@code N} screen lines; otherwise it will use {@code N+1} lines.
         * When the cursor position is the right margin of the last line
         * (i.e. after {@code N*columns} normal characters), if this option
         * it set, the cursor will be remain on the last line (line {@code N-1},
         * zero-origin); if unset the cursor will be on the empty next line.
         * Regardless, for all except the last screen line if the cursor is at
         * the right margin, it will be shown at the start of the next line.
         */
        DELAY_LINE_WRAP,
        AUTO_PARAM_SLASH(true),
        AUTO_REMOVE_SLASH(true),
        INSERT_TAB(true),
        MOUSE;

        private final boolean def;

        Option() {
            this(false);
        }

        Option(boolean def) {
            this.def = def;
        }

        public boolean isDef() {
            return def;
        }
    }

    enum RegionType {
        NONE,
        CHAR,
        LINE
    }

    /**
     * Read the next line and return the contents of the buffer.
     *
     * Equivalent to <code>readLine(null, null, null)</code>
     */
    String readLine() throws UserInterruptException, EndOfFileException;

    /**
     * Read the next line with the specified character mask. If null, then
     * characters will be echoed. If 0, then no characters will be echoed.
     *
     * Equivalent to <code>readLine(null, mask, null)</code>
     */
    String readLine(Character mask) throws UserInterruptException, EndOfFileException;

    /**
     * Read the next line with the specified prompt.
     * If null, then the default prompt will be used.
     *
     * Equivalent to <code>readLine(prompt, null, null)</code>
     */
    String readLine(String prompt) throws UserInterruptException, EndOfFileException;

    /**
     * Read a line from the <i>in</i> {@link InputStream}, and return the line
     * (without any trailing newlines).
     *
     * Equivalent to <code>readLine(prompt, mask, null)</code>
     */
    String readLine(String prompt, Character mask) throws UserInterruptException, EndOfFileException;

    /**
     * Read a line from the <i>in</i> {@link InputStream}, and return the line
     * (without any trailing newlines).
     *
     * @param prompt    The prompt to issue to the terminal, may be null.
     *   This is a template, with optional {@code '%'} escapes, as
     *   described in the class header.
     * @param mask      The character mask, may be null.
     * @param buffer    The default value presented to the user to edit, may be null.
     * @return          A line that is read from the terminal, can never be null.
     *
     * Equivalent to <code>readLine(prompt, null, mask, buffer)</code>
     */
    String readLine(String prompt, Character mask, String buffer) throws UserInterruptException, EndOfFileException;

    /**
     * Read a line from the <i>in</i> {@link InputStream}, and return the line
     * (without any trailing newlines).
     *
     * @param prompt      The prompt to issue to the terminal, may be null.
     *   This is a template, with optional {@code '%'} escapes, as
     *   described in the class header.
     * @param rightPrompt The right prompt
     *   This is a template, with optional {@code '%'} escapes, as
     *   described in the class header.
     * @param mask        The character mask, may be null.
     * @param buffer      The default value presented to the user to edit, may be null.
     * @return            A line that is read from the terminal, can never be null.
     *
     * @throws UserInterruptException if readLine was interrupted (using Ctrl-C for example)
     * @throws EndOfFileException if an EOF has been found (using Ctrl-D for example)
     * @throws java.io.IOError in case of other i/o errors
     */
    String readLine(String prompt, String rightPrompt, Character mask, String buffer) throws UserInterruptException, EndOfFileException;

    void callWidget(String name);

    Map<String, Object> getVariables();

    Object getVariable(String name);

    void setVariable(String name, Object value);

    boolean isSet(Option option);

    void setOpt(Option option);

    void unsetOpt(Option option);

    Terminal getTerminal();

    Map<String, Widget> getWidgets();

    Map<String, Widget> getBuiltinWidgets();

    BufferImpl getBuffer();

    void runMacro(String macro);

    History getHistory();

    Parser getParser();

    Highlighter getHighlighter();

    Expander getExpander();

    Map<String, KeyMap<Binding>> getKeyMaps();

    ParsedLine getParsedLine();

    String getSearchTerm();

    RegionType getRegionActive();

    int getRegionMark();

}