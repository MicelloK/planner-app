import React from "react";
import "../styles/ControlPanel.css";

function ControlPanelHeader() {
     return (
          <div className="control-panel-header">
               <h1>Planner app</h1>
          </div>
     );
}

function CallendarList({callendarsList, currentCallendar, setCallendar}) {
     const handleChange = (event) => {
          setCallendar(event.target.value);
     };

     return (
          <div className="calendars-wrapper">
               <select value={callendarsList[currentCallendar]} onChange={handleChange}>
                    {callendarsList.map((calendar) => (
                         <option key={calendar.id} value={calendar.id}>
                              {calendar.name}
                         </option>
                    ))}
               </select>
          </div>
     );
}

function NewAppointment() {
     return (
          <div className="new-appointment">
               <button className="new-appointment-button">Utwórz</button>
          </div>
     );
}

function AppointmentInfo({appointment}) {
     const monthsPl = {
          1: "stycznia",
          2: "lutego",
          3: "marca",
          4: "kwietnia",
          5: "maja",
          6: "czerwca",
          7: "lipca",
          8: "sierpnia",
          9: "września",
          10: "października",
          11: "listopada",
          12: "grudnia"
     }

     const convertTime = (time) => {
          return time < 10 ? "0" + time : time;
     }

     return (
          <div className="appointment-info-wrapper">
               <div className="appointment-info">
                    <div className="appointment-info-body">
                         <div className="appointment-info-desc">
                              <h2>{appointment.name}</h2>
                              <div className="appointment-info-date">
                                   <p>Data: {appointment.startTime.getDate()}. {monthsPl[appointment.startTime.getMonth()+1]} {appointment.startTime.getFullYear()}r.<br/>
                                   Godzina: {convertTime(appointment.startTime.getHours())}:{convertTime(appointment.startTime.getMinutes())} - {convertTime(appointment.endTime.getHours())}:{convertTime(appointment.endTime.getMinutes())}</p>
                                   <p>Uczestnicy:</p>
                                   <ul>
                                        {appointment.participants.map((participant, index) => (
                                             <li key={index}>{participant}</li>
                                        ))}
                                   </ul>
                                   <p>Opis:</p>
                                   <p>{appointment.desc}</p>
                                   <p>Utworzone przez:<br/><em>{appointment.author}</em></p>
                              </div>
                         </div>
                         <div className="appointment-info-settings-wrapper">
                              <button className="appointment-info-settings-btn">Edytuj</button>
                         </div>
                    </div>
               </div>
          </div>
     );
}

function Settings() {
     return (
          <div className="settings">
               <button className="settings-button">Ustawienia</button>
          </div>
     );
}

function ControlPanelBody({callendarsList, currentAppointment, currentCallendar, setCallendar}) {
     return (
          <div className="control-panel-body">
               <CallendarList callendarsList={callendarsList} currentCallendar={currentCallendar} setCallendar={setCallendar}/>
               <NewAppointment />
               <AppointmentInfo appointment={currentAppointment}/>
               <Settings />
          </div>
     );
}

export default function ControlPanel({callendarsList, currentAppointment, currentCallendar, setCallendar}) {
     return (
          <div className="control-panel">
               <ControlPanelHeader />
               <ControlPanelBody callendarsList={callendarsList} currentAppointment={currentAppointment} currentCallendar={currentCallendar} setCallendar={setCallendar}/>
          </div>
     );
}