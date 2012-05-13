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

package uk.ac.manchester.cs.owl.owlapi;

import java.util.Collection;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLAxiomVisitorEx;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 26-Oct-2006<br><br>
 */
@SuppressWarnings("javadoc")
public class OWLClassAssertionImpl extends OWLIndividualAxiomImpl implements OWLClassAssertionAxiom {


	private static final long serialVersionUID = 1631947003263619315L;

	private final OWLIndividual individual;

    private final OWLClassExpression classExpression;


    public OWLClassAssertionImpl(OWLIndividual individual, OWLClassExpression classExpression, Collection<? extends OWLAnnotation> annotations) {
        super(annotations);
        this.individual = individual;
        this.classExpression = classExpression;
    }

    public OWLClassAssertionAxiom getAxiomWithoutAnnotations() {
        if (!isAnnotated()) {
            return this;
        }
        return getOWLDataFactory().getOWLClassAssertionAxiom(getClassExpression(), getIndividual());
    }

    public OWLClassAssertionAxiom getAnnotatedAxiom(Set<OWLAnnotation> annotations) {
        return getOWLDataFactory().getOWLClassAssertionAxiom(getClassExpression(), getIndividual(), mergeAnnos(annotations));
    }

    public OWLClassExpression getClassExpression() {
        return classExpression;
    }


    public OWLIndividual getIndividual() {
        return individual;
    }


    @Override
	public boolean equals(Object obj) {
        if (super.equals(obj)) {
            if (!(obj instanceof OWLClassAssertionAxiom)) {
                return false;
            }
            OWLClassAssertionAxiom other = (OWLClassAssertionAxiom) obj;
            return other.getIndividual().equals(individual) && other.getClassExpression().equals(classExpression);
        }
        return false;
    }

    public OWLSubClassOfAxiom asOWLSubClassOfAxiom() {
        return getOWLDataFactory().getOWLSubClassOfAxiom(getOWLDataFactory().getOWLObjectOneOf(getIndividual()), getClassExpression());
    }

    public void accept(OWLAxiomVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }

    public <O> O accept(OWLAxiomVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    public AxiomType<?> getAxiomType() {
        return AxiomType.CLASS_ASSERTION;
    }

    @Override
	protected int compareObjectOfSameType(OWLObject object) {
        OWLClassAssertionAxiom otherAx = (OWLClassAssertionAxiom) object;
        int diff = getIndividual().compareTo(otherAx.getIndividual());
        if (diff != 0) {
            return diff;
        }
        else {
            return getClassExpression().compareTo(otherAx.getClassExpression());
        }
    }

}
