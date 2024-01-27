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
        'Authorization': 'Basic dXNlcjplYzRkYWM2MS0yODZlLTQ4YzAtYjJhNy0yYTM0MzFiNDNkM2M='
      }), 
    };
    
    this.http.get('http://localhost:8080/api/pizzas/available', requestOptions).subscribe(response => {
      this.pizzas = response;
    });
  }
}
