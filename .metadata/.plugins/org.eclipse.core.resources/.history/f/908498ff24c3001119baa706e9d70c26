package com.actionbarsherlock.internal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static com.actionbarsherlock.internal.ActionBarSherlockCompat.cleanActivityName;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class ManifestParsingTest {
    @Test
    public void testFullyQualifiedClassName() {
        String expected = "com.other.package.SomeClass";
        String actual = cleanActivityName("com.jakewharton.test", "com.other.package.SomeClass");
        assertThat(expected, equalTo(actual));
    }

    @Test
    public void testFullyQualifiedClassNameSamePackage() {
        String expected = "com.jakewharton.test.SomeClass";
        String actual = cleanActivityName("com.jakewharton.test", "com.jakewharton.test.SomeClass");
        assertThat(expected, equalTo(actual));
    }

    @Test
    public void testUnqualifiedClassName() {
        String expected = "com.jakewharton.test.SomeClass";
        String actual = cleanActivityName("com.jakewharton.test", "SomeClass");
        assertThat(expected, equalTo(actual));
    }

    @Test
    public void testRelativeClassName() {
        String expected = "com.jakewharton.test.ui.SomeClass";
        String actual = cleanActivityName("com.jakewharton.test", ".ui.SomeClass");
        assertThat(expected, equalTo(actual));
    }
}