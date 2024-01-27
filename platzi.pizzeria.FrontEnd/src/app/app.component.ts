import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  pizzas: any;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const requestOptions = {                                                                                                                                                                                 
      headers: new HttpHeaders({
        'Authorization': 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlzcyI6InBsYXR6aS1waXp6YSIsImV4cCI6MTcwNzY4MDk3MSwiaWF0IjoxNzA2Mzg0OTcxfQ._gqtTPmQdD3mAnRYOy6dP1r3d4WLoC60ymGR1DAUHLo'
      }), 
    };
    
    this.http.get('http://localhost:8080/api/pizzas/available', requestOptions).subscribe(response => {
      this.pizzas = response;
    });
  }
}
