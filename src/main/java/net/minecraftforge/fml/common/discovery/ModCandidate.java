/*
 * Forge Mod Loader
 * Copyright (c) 2012-2013 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     cpw - implementation
 */

package net.minecraftforge.fml.common.discovery;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraftforge.fml.common.ModContainer;

import java.io.File;
import java.util.List;
import java.util.Set;


public class ModCandidate {
    private final File classPathRoot;
    private final File modContainer;
    private final ContainerType sourceType;
    private final boolean classpath;
    private final boolean isMinecraft;
    private final Set<String> foundClasses = Sets.newHashSet();
    private List<ModContainer> mods;
    private final List<String> packages = Lists.newArrayList();
    private ASMDataTable table;

    public ModCandidate(File classPathRoot, File modContainer, ContainerType sourceType) {
        this(classPathRoot, modContainer, sourceType, false, false);
    }

    public ModCandidate(File classPathRoot, File modContainer, ContainerType sourceType, boolean isMinecraft, boolean classpath) {
        this.classPathRoot = classPathRoot;
        this.modContainer = modContainer;
        this.sourceType = sourceType;
        this.isMinecraft = isMinecraft;
        this.classpath = classpath;
    }

    public File getClassPathRoot() {
        return classPathRoot;
    }

    public File getModContainer() {
        return modContainer;
    }

    public ContainerType getSourceType() {
        return sourceType;
    }

    public List<ModContainer> explore(ASMDataTable table) {
        this.table = table;
        this.mods = sourceType.findMods(this, table);
        return this.mods;
    }

    public void addClassEntry(String name) {
        String className = name.substring(0, name.lastIndexOf('.')); // strip the .class
        foundClasses.add(className);
        className = className.replace('/', '.');
        int pkgIdx = className.lastIndexOf('.');
        if (pkgIdx > -1) {
            String pkg = className.substring(0, pkgIdx);
            packages.add(pkg);
            this.table.registerPackage(this, pkg);
        }
    }

    public boolean isClasspath() {
        return classpath;
    }

    public boolean isMinecraftJar() {
        return isMinecraft;
    }

    public Set<String> getClassList() {
        return foundClasses;
    }

    public List<ModContainer> getContainedMods() {
        return mods;
    }

    public List<String> getContainedPackages() {
        return packages;
    }
}
