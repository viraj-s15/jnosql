/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.eclipse.jnosql.mapping.reflection;

import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.VetedConverter;
import org.eclipse.jnosql.mapping.test.entities.Actor;
import org.eclipse.jnosql.mapping.test.entities.Download;
import org.eclipse.jnosql.mapping.test.entities.Movie;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.Vendor;
import org.eclipse.jnosql.mapping.test.entities.Worker;
import org.eclipse.jnosql.mapping.test.entities.inheritance.EmailNotification;
import org.eclipse.jnosql.mapping.test.entities.inheritance.LargeProject;
import org.eclipse.jnosql.mapping.test.entities.inheritance.Notification;
import org.eclipse.jnosql.mapping.test.entities.inheritance.Project;
import org.eclipse.jnosql.mapping.test.entities.inheritance.SmallProject;
import org.eclipse.jnosql.mapping.test.entities.inheritance.SmsNotification;
import org.eclipse.jnosql.mapping.test.entities.inheritance.SocialMediaNotification;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.lang.reflect.Field;
import java.util.Optional;


import static org.eclipse.jnosql.mapping.DiscriminatorColumn.DEFAULT_DISCRIMINATOR_COLUMN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@EnableAutoWeld
@AddPackages(value = Convert.class)
@AddPackages(value = VetedConverter.class)
@AddExtensions(EntityMetadataExtension.class)
public class ReflectionsTest {


    @Inject
    private Reflections reflections;

    @Test
    public void shouldReturnsEntityName() {
        assertEquals("Person", reflections.getEntityName(Person.class));
        assertEquals("movie", reflections.getEntityName(Movie.class));
    }

    @Test
    public void shouldListFields() {

        assertEquals(4, reflections.getFields(Person.class).size());
        assertEquals(6, reflections.getFields(Actor.class).size());

    }

    @Test
    public void shouldReturnColumnName() throws NoSuchFieldException {
        Field phones = Person.class.getDeclaredField("phones");
        Field id = Person.class.getDeclaredField("id");

        assertEquals("phones", reflections.getColumnName(phones));
        assertEquals("id", reflections.getColumnName(id));
        assertEquals("_id", reflections.getIdName(id));
    }

    @Test
    public void shouldGetEntityNameWhenThereIsNoAnnotation(){
        String entityName = reflections.getEntityName(Person.class);
        assertEquals(Person.class.getSimpleName(), entityName);
    }

    @Test
    public void shouldGetEntityNameFromAnnotation() {
        String entityName = reflections.getEntityName(Download.class);
        assertEquals("download", entityName);
        assertEquals("vendors", reflections.getEntityName(Vendor.class));
    }

    @Test
    public void shouldGetEntityFromInheritance() {
        assertEquals("Notification", reflections.getEntityName(SocialMediaNotification.class));
        assertEquals("Notification", reflections.getEntityName(SmsNotification.class));
        assertEquals("Notification", reflections.getEntityName(EmailNotification.class));

        assertEquals("Project", reflections.getEntityName(LargeProject.class));
        assertEquals("Project", reflections.getEntityName(SmallProject.class));
    }

    @Test
    public void shouldReturnEmptyGetInheritance() {
        Optional<InheritanceMetadata> inheritance = this.reflections.getInheritance(Person.class);
        assertTrue(inheritance.isEmpty());
    }

    @Test
    public void shouldReturnGetInheritance() {
        Optional<InheritanceMetadata> inheritance = this.reflections.getInheritance(LargeProject.class);
        assertFalse(inheritance.isEmpty());
        InheritanceMetadata project = inheritance.get();
        assertEquals("size", project.getDiscriminatorColumn());
        assertEquals("Large", project.getDiscriminatorValue());
        assertEquals(Project.class, project.getParent());
        assertEquals(LargeProject.class, project.getEntity());
    }

    @Test
    public void shouldReturnGetInheritanceWithoutColumn() {
        Optional<InheritanceMetadata> inheritance = this.reflections.getInheritance(SmsNotification.class);
        assertFalse(inheritance.isEmpty());
        InheritanceMetadata project = inheritance.get();
        assertEquals(DEFAULT_DISCRIMINATOR_COLUMN, project.getDiscriminatorColumn());
        assertEquals("SMS", project.getDiscriminatorValue());
        assertEquals(Notification.class, project.getParent());
        assertEquals(SmsNotification.class, project.getEntity());
    }

    @Test
    public void shouldReturnGetInheritanceWithoutDiscriminatorValue() {
        Optional<InheritanceMetadata> inheritance = this.reflections.getInheritance(SocialMediaNotification.class);
        assertFalse(inheritance.isEmpty());
        InheritanceMetadata project = inheritance.get();
        assertEquals(DEFAULT_DISCRIMINATOR_COLUMN, project.getDiscriminatorColumn());
        assertEquals("SocialMediaNotification", project.getDiscriminatorValue());
        assertEquals(Notification.class, project.getParent());
        assertEquals(SocialMediaNotification.class, project.getEntity());
    }

    @Test
    public void shouldGetInheritanceParent() {
        Optional<InheritanceMetadata> inheritance = this.reflections.getInheritance(Project.class);
        assertFalse(inheritance.isEmpty());
        InheritanceMetadata project = inheritance.get();
        assertEquals("size", project.getDiscriminatorColumn());
        assertEquals("Project", project.getDiscriminatorValue());
        assertEquals(Project.class, project.getParent());
        assertEquals(Project.class, project.getEntity());
    }

    @Test
    public void shouldReturnHasInheritanceAnnotation() {
        assertFalse(this.reflections.hasInheritanceAnnotation(Person.class));
        assertFalse(this.reflections.hasInheritanceAnnotation(Worker.class));
        assertFalse(this.reflections.hasInheritanceAnnotation(SmsNotification.class));
        assertFalse(this.reflections.hasInheritanceAnnotation(SmallProject.class));

        assertTrue(this.reflections.hasInheritanceAnnotation(Notification.class));
        assertTrue(this.reflections.hasInheritanceAnnotation(Project.class));
    }


}