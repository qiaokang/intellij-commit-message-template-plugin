/*
 * Copyright 2014 Darek Kay <darekkay@eclectide.com>
 *
 * MIT license
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package commitmessagetemplate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.CommitMessageI;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.ui.Refreshable;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author darekkay
 */
public class CommitMessageTemplateAction extends AnAction implements DumbAware {

    @Override
    public void actionPerformed(AnActionEvent e)
    {
        final CommitMessageI checkinPanel = getCheckinPanel(e);
        if (checkinPanel == null)
        {
            return;
        }

        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        CommitMessageTemplateConfig config =
                CommitMessageTemplateConfig.getInstance(project);

        if (config != null)
        {
            String commitMessage = config.getCommitMessage();
            if (!commitMessage.isEmpty())
            {
                commitMessage = commitMessage.replace("${date}", new
                    SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss").format(new Date()));
                checkinPanel.setCommitMessage(commitMessage);
            }
        }
    }

    @Nullable
    private static CommitMessageI getCheckinPanel(@Nullable AnActionEvent e)
    {
        if (e == null)
        {
            return null;
        }
        Refreshable data = Refreshable.PANEL_KEY.getData(e.getDataContext());
        if (data instanceof CommitMessageI)
        {
            return (CommitMessageI) data;
        }
        CommitMessageI commitMessageI =
                VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(e.getDataContext());
        if (commitMessageI != null)
        {
            return commitMessageI;
        }
        return null;
    }
}
