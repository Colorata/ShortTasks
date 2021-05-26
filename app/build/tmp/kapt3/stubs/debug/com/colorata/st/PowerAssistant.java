package com.colorata.st;

import java.lang.System;

@androidx.annotation.RequiresApi(value = 30)
@kotlin.Metadata(mv = {1, 4, 2}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J.\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u00052\b\b\u0001\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\rH\u0007J\u001c\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00070\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00050\u0017H\u0016J\u000e\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00070\u0015H\u0016J&\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\u001d2\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00120\u001fH\u0017R \u0010\u0003\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/colorata/st/PowerAssistant;", "Landroid/service/controls/ControlsProviderService;", "()V", "controlFlows", "", "", "Lio/reactivex/processors/ReplayProcessor;", "Landroid/service/controls/Control;", "controlList", "", "getControlList", "()Ljava/util/List;", "flashlightOn", "", "addControls", "name", "title", "icon", "", "enabled", "createPublisherFor", "Ljava/util/concurrent/Flow$Publisher;", "controlIds", "", "createPublisherForAllAvailable", "performControlAction", "", "controlId", "action", "Landroid/service/controls/actions/ControlAction;", "consumer", "Ljava/util/function/Consumer;", "app_debug"})
public final class PowerAssistant extends android.service.controls.ControlsProviderService {
    private final java.util.Map<java.lang.String, io.reactivex.processors.ReplayProcessor<android.service.controls.Control>> controlFlows = null;
    private boolean flashlightOn = false;
    
    @org.jetbrains.annotations.NotNull()
    @android.annotation.SuppressLint(value = {"WrongConstant", "UnspecifiedImmutableFlag"})
    public final android.service.controls.Control addControls(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @androidx.annotation.DrawableRes()
    int icon, boolean enabled) {
        return null;
    }
    
    private final java.util.List<android.service.controls.Control> getControlList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.concurrent.Flow.Publisher<android.service.controls.Control> createPublisherForAllAvailable() {
        return null;
    }
    
    @android.annotation.SuppressLint(value = {"WrongConstant"})
    @java.lang.Override()
    public void performControlAction(@org.jetbrains.annotations.NotNull()
    java.lang.String controlId, @org.jetbrains.annotations.NotNull()
    android.service.controls.actions.ControlAction action, @org.jetbrains.annotations.NotNull()
    java.util.function.Consumer<java.lang.Integer> consumer) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.util.concurrent.Flow.Publisher<android.service.controls.Control> createPublisherFor(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> controlIds) {
        return null;
    }
    
    public PowerAssistant() {
        super();
    }
}