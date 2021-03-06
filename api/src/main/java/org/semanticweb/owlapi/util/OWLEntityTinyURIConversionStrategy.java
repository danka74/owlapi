/*
 * This file is part of the OWL API.
 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, University of Manchester
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
package org.semanticweb.owlapi.util;

import java.util.HashMap;
import java.util.Map;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityVisitor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * Provides an entity URI conversion strategy which converts entity URIs to a
 * common base and alpha-numeric fragment. The fragment is of the form An, where
 * n is an integer (starting at 1), and A is a string which depends on the type
 * of entity:
 * <ul>
 * <li>For classes: A = "C"</li>
 * <li>For object properties: A = "op"</li>
 * <li>For data properties: A = "dp"</li>
 * <li>For individuals: A = "i"</li>
 * </ul>
 * 
 * @author Matthew Horridge, The University Of Manchester, Bio-Health
 *         Informatics Group, Date: 25-Nov-2007
 */
public class OWLEntityTinyURIConversionStrategy implements
        OWLEntityURIConverterStrategy {

    /** default base. */
    public static final String DEFAULT_BASE = "http://tinyname.org#";
    private final String base;
    private final Map<OWLEntity, IRI> entityNameMap;
    private final OWLEntityFragmentProvider fragmentProvider;

    /**
     * Constructs an entity URI converter strategy, where the base of the
     * generated URIs corresponds to the value specified by the DEFAULT_BASE
     * constant.
     */
    public OWLEntityTinyURIConversionStrategy() {
        this(DEFAULT_BASE);
    }

    /**
     * Constructs an entity URI converter strategy, where the specified base is
     * used for the base of the URIs generated by the generator.
     * 
     * @param base
     *        The base to be used.
     */
    public OWLEntityTinyURIConversionStrategy(String base) {
        this.base = base;
        entityNameMap = new HashMap<OWLEntity, IRI>();
        fragmentProvider = new OWLEntityFragmentProvider();
    }

    @Override
    public IRI getConvertedIRI(OWLEntity entity) {
        IRI iri = entityNameMap.get(entity);
        if (iri != null) {
            return iri;
        }
        if (entity instanceof OWLDatatype) {
            return entity.getIRI();
        }
        String name = fragmentProvider.getName(entity);
        iri = IRI.create(base, name);
        entityNameMap.put(entity, iri);
        return iri;
    }

    private static class OWLEntityFragmentProvider implements OWLEntityVisitor {

        private String name;
        private int classCount = 0;
        private int objectPropertyCount = 0;
        private int dataPropertyCount = 0;
        private int individualCount = 0;
        private int annotationPropertyCount = 0;
        private int datatypeCount = 0;

        public OWLEntityFragmentProvider() {}

        public String getName(OWLEntity entity) {
            if (entity.isBuiltIn()) {
                return entity.getIRI().toString();
            }
            entity.accept(this);
            return name;
        }

        @Override
        public void visit(OWLClass cls) {
            classCount++;
            name = "C" + classCount;
        }

        @Override
        public void visit(OWLDatatype datatype) {
            datatypeCount++;
            name = "dt" + datatypeCount;
        }

        @Override
        public void visit(OWLNamedIndividual individual) {
            individualCount++;
            name = "i" + individualCount;
        }

        @Override
        public void visit(OWLDataProperty property) {
            dataPropertyCount++;
            name = "dp" + dataPropertyCount;
        }

        @Override
        public void visit(OWLObjectProperty property) {
            objectPropertyCount++;
            name = "op" + objectPropertyCount;
        }

        @Override
        public void visit(OWLAnnotationProperty property) {
            annotationPropertyCount++;
            name = "ap" + annotationPropertyCount;
        }
    }
}
