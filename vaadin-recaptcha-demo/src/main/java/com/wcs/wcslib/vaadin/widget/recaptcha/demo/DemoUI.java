/*
 * Copyright 2013 kumm.
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
package com.wcs.wcslib.vaadin.widget.recaptcha.demo;

import com.vaadin.annotations.JavaScript;
import com.wcs.wcslib.vaadin.widget.recaptcha.ReCaptcha;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import javax.servlet.annotation.WebServlet;
import com.wcs.wcslib.vaadin.widget.recaptcha.shared.ReCaptchaOptions;

@Title("ReCaptcha Add-on Demo")
@SuppressWarnings("serial")
@JavaScript("http://www.google.com/recaptcha/api/js/recaptcha_ajax.js")
public class DemoUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, 
            widgetset = "com.wcs.wcslib.vaadin.widget.recaptcha.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout layout = new VerticalLayout();
        final ConfigComponent configComponent = new ConfigComponent();

        layout.addComponent(configComponent);
        layout.setSpacing(true);
        layout.setMargin(true);

        Button showBtn = new Button("SHOW", new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                layout.removeAllComponents();
                ReCaptcha reCaptcha = createCaptcha(configComponent);
                Button validateBtn = createValidateBtn(reCaptcha);
                layout.addComponent(reCaptcha);
                Button reconfigBtn = createReconfigBtn();
                HorizontalLayout actionsLayout = new HorizontalLayout(validateBtn, reconfigBtn);
                actionsLayout.setSpacing(true);
                layout.addComponent(actionsLayout);
            }
        });
        
        HorizontalLayout btnLayout = new HorizontalLayout(
                showBtn, 
                new Link("Recaptcha API docs",
                        new ExternalResource("https://developers.google.com/recaptcha/docs/customization")));
        btnLayout.setSpacing(true);
        
        layout.addComponent(btnLayout);
        setContent(layout);
    }

    private ReCaptcha createCaptcha(final ConfigComponent config) {
        return new ReCaptcha(
                "6LeIA-oSAAAAAIYbT--2nA_mJq-LhUlNyCZZE4jo",
                "6LeIA-oSAAAAAO93gixbSw3lp4Om6w8H3UVpUQL_",
                new ReCaptchaOptions() {
                    {
                        theme = config.getTheme();
                        lang = config.getLang();
                        custom_translations = config.getCustomTranslations();
                        custom_theme_widget = config.getCustomThemeWidget();
                    }
                },
                config.getCustomHtml()
        );
    }

    private Button createValidateBtn(final ReCaptcha reCaptcha) {
        return new Button("VALIDATE", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!reCaptcha.isValid()) {
                    Notification.show("Invalid!", Notification.Type.ERROR_MESSAGE);
                } else {
                    Notification.show("Success!");
                }
            }
        });
    }

    private Button createReconfigBtn() {
        Button reconfigBtn = new Button("ReConfigure", new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Page.getCurrent().reload();
            }
        });
        reconfigBtn.setStyleName(Reindeer.BUTTON_LINK);
        return reconfigBtn;
    }

}
