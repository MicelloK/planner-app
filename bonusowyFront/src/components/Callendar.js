import React from "react";
import "../styles/Callendar.css";

function CallendarHeader({selectedDate, setStartDate}) {
     const monthsPL = {
          1: "Styczeń",
          2: "Luty",
          3: "Marzec",
          4: "Kwiecień",
          5: "Maj",
          6: "Czerwiec",
          7: "Lipiec",
          8: "Sierpień",
          9: "Wrzesień",
          10: "Październik",
          11: "Listopad",
          12: "Grudzień"
     }

     const handleClickLeft = () => {
          const newDate = new Date(selectedDate.getFullYear(), selectedDate.getMonth(), selectedDate.getDate() - 7);
          setStartDate(newDate);
     }

     const handleClickRight = () => {
          const newDate = new Date(selectedDate.getFullYear(), selectedDate.getMonth(), selectedDate.getDate() + 7);
          setStartDate(newDate);
     }

     return (
          <div className="callendar-header">
               <button className="callendar-header-button" onClick={handleClickLeft}> &lt; </button>
               <h1>{monthsPL[selectedDate.getMonth()+1]} {selectedDate.getFullYear()}</h1>
               <button className="callendar-header-button" onClick={handleClickRight}> &gt; </button>
          </div>
     );
}

function DayHeader({dayNumber}) {
     return (
          <div className="day-header">
               <div className="day-label">{dayNumber}</div>
          </div>
     );
}

function DayCallendar({appointments, setCurrentAppointment}) {
     function handleClick(appointmentId) {
          setCurrentAppointment(appointmentId);
     }

     const generateAppointmentDivs = (appointments, index) => {
          return appointments.map((appointment) => {
               console.log(appointment);
               const start = appointment.startTime;
               const end = appointment.endTime; 
               const startHeight = start.getHours() * 60 + start.getMinutes();
               const endHeight = end.getHours() * 60 + end.getMinutes();
               const height = (endHeight - startHeight) / (24 * 60);
          
               const top = startHeight / (24 * 60); // percent of all day
          
               const divStyle = {
               position: 'absolute',
               top: `calc(155px + (100% - 155px)*${top})`, // Im sorry for that
               height: `calc((100% - 155px)*${height})`,
               background: `${appointment.color}`
               };

               const convertTime = (time) => {
                    return time < 10 ? "0" + time : time;
               }
          
               return (
               <button className="appointment-button" key={index} style={divStyle} onClick={() => handleClick(appointment.id)}>
                    <p>{appointment.name}, ({appointment.startTime.getHours()}:{convertTime(appointment.startTime.getMinutes())}-{appointment.endTime.getHours()}:{convertTime(appointment.endTime.getMinutes())})</p>
               </button>
          );
          });
     };
     
     return (
     <div className="day-calendar">
          {generateAppointmentDivs(appointments)}
     </div>
     );
}

function DayObject({dayNumber, alphaType, appointments, setCurrentAppointment}) {
     const alpha = 0.6 + 0.05 * alphaType;
     const style = {
          backgroundColor: `rgba(99, 136, 137, ${alpha})`
     };

     return (
          <div className="day-object" style={style}>
               <DayHeader dayNumber={dayNumber} />
               <DayCallendar appointments={appointments} setCurrentAppointment={setCurrentAppointment} />
          </div>
     );
}

function HoursList() {
     const hours = Array.from({ length: 24 }, (_, index) => index);

     return (
     <div className="hours-list">
          <div className="hour-header"></div>
          <div className="hour-list-body">
          {hours.map((hour) => (
               <div key={hour} className="hour-label">
               {hour < 10 ? `0${hour}:00` : `${hour}:00`}
               </div>
          ))}
          <div className="hour-label"></div>
          </div>
     </div>
     );
}

function DaysList({startDate, appointments, setCurrentAppointment}) {
     const selectedWeek = [7];
     const appointmentsOn = [7];

     const equalDate = (date1, date2) => {
          return date1.getDate() === date2.getDate() && date1.getMonth() === date2.getMonth() && date1.getFullYear() === date2.getFullYear();    
     }

     for (let i = 0; i < 7; i++) {
          selectedWeek[i] = new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate() + i).getDate();
          

          appointmentsOn[i] = [];
          appointments.forEach((appointment) => {
               if (equalDate(appointment.startTime, new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate() + i))) {
                    appointmentsOn[i].push(appointment);
               }
          });
     }

     return (
          <div className="days-list">
            <HoursList />
            {selectedWeek.map((dayNumber, index) => (
              <DayObject key={index} dayNumber={dayNumber} alphaType={index + 1} appointments={appointmentsOn[index]} setCurrentAppointment={setCurrentAppointment}/>
            ))}
          </div>
        );
}

function CallendarBody({startDate, appointments, setCurrentAppointment}) {
     return (
          <div className="callendar-body">
               <DaysList startDate={startDate} appointments={appointments} setCurrentAppointment={setCurrentAppointment}/>
          </div>
     );
}


export default function Callendar({appointments, setCurrentAppointment}) {
     const [startDate, setStartDate] = React.useState(new Date());

     return (
          <div className="callendar">
               <CallendarHeader selectedDate={startDate} setStartDate={setStartDate}/>
               <CallendarBody startDate={startDate} appointments={appointments} setCurrentAppointment={setCurrentAppointment}/>
          </div>
     );
}