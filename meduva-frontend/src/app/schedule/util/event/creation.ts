import {CalendarEvent} from "angular-calendar";

export function createUnavailabilityEvent(start: Date, end: Date): CalendarEvent {
  return {
    draggable: false,
    end: new Date(end),
    id: undefined,
    meta: undefined,
    start: new Date(start),
    title: "Unavailable",
    color: {
      primary: "#FF9191",
      secondary: "#FF9191"
    },
    cssClass: "custom-event",
  };
}

export function createOffWorkHoursEvent(start: Date, end: Date): CalendarEvent {

  return {
    draggable: false,
    end: new Date(end),
    id: undefined,
    meta: undefined,
    start: new Date(start),
    title: "Off work",
    color: {
      primary: "lightGray",
      secondary: "lightGray"
    },
    cssClass: "custom-event",
  };
}

export function createAbsenceHoursEvent(start: Date, end: Date): CalendarEvent {

  return {
    draggable: false,
    end: new Date(end),
    id: undefined,
    meta: undefined,
    start: new Date(start),
    title: "Absence",
    color: {
      primary: "#FF9191",
      secondary: "#FF9191",
    },
    cssClass: "custom-event",
  };
}

export function createVisitsAsWorkerEvent(start: Date, end: Date): CalendarEvent {

  return {
    draggable: false,
    end: new Date(end),
    id: undefined,
    meta: undefined,
    start: new Date(start),
    title: "Visit",
    color: {
      primary: "#4F4C79",
      secondary: "#4F4C79"
    },
    cssClass: "custom-event",
  };
}

export function createVisitsAsClientEvent(start: Date, end: Date): CalendarEvent {

  return {
    draggable: false,
    end: new Date(end),
    id: undefined,
    meta: undefined,
    start: new Date(start),
    title: "Visit as client",
    color: {
      primary: "#4438e5",
      secondary: "#4438e5"
    },
    cssClass: "custom-event",
  };
}
