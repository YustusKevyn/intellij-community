/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.ide.structureView.impl.xml;

import com.intellij.ide.structureView.StructureViewExtension;
import com.intellij.ide.structureView.StructureViewFactoryEx;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.lang.dtd.DTDLanguage;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class XmlStructureViewTreeModel extends TextEditorBasedStructureViewModel{
  private final XmlFile myFile;
  private static final Class[] myClasses = new Class[]{XmlTag.class, XmlFile.class, XmlEntityDecl.class, XmlElementDecl.class, XmlAttlistDecl.class, XmlConditionalSection.class};
  private static final Sorter[] mySorters = {Sorter.ALPHA_SORTER};

  public XmlStructureViewTreeModel(XmlFile file, @Nullable Editor editor) {
    super(editor, file);
    myFile = file;
  }

  @Override
  @NotNull
  public StructureViewTreeElement getRoot() {
    if (myFile.getLanguage() == DTDLanguage.INSTANCE) return new DtdFileTreeElement(myFile);
    return new XmlFileTreeElement(myFile);
  }

  @Override
  public boolean shouldEnterElement(final Object element) {
    return element instanceof XmlTag && ((XmlTag)element).getSubTags().length > 0;
  }

  @Override
  protected PsiFile getPsiFile() {
    return myFile;
  }

  @Override
  @NotNull
  protected Class[] getSuitableClasses() {
    return myClasses;
  }

  @Override
  public Object getCurrentEditorElement() {
    final Object editorElement = super.getCurrentEditorElement();
    if (editorElement instanceof XmlTag) {
      final Collection<StructureViewExtension> structureViewExtensions =
          StructureViewFactoryEx.getInstanceEx(myFile.getProject()).getAllExtensions(XmlTag.class);
      for(StructureViewExtension extension:structureViewExtensions) {
        final Object element = extension.getCurrentEditorElement(getEditor(), (PsiElement)editorElement);
        if (element != null) return element;
      }
    }
    return editorElement;
  }

  @Override
  @NotNull
  public Sorter[] getSorters() {
    return mySorters;
  }
}