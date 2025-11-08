import { Component, OnInit } from '@angular/core';
import { CommissionService } from '../../../Services/Commission/commission-service';
import { ActivatedRoute } from '@angular/router';
import Commission from '../../../Models/Commission/commission';

@Component({
  selector: 'app-commission-student-view',
  imports: [],
  templateUrl: './commission-student-view.html',
  styleUrl: './commission-student-view.css',
})
export class CommissionStudentView implements OnInit {
  programName!: string;
  commissionList!: Commission[];

  constructor(
    private commissionService: CommissionService,
    private activatedRoute: ActivatedRoute
  ) {
    this.programName = this.activatedRoute.snapshot.params['name'];
  }

  ngOnInit(): void {
    this.getCommissionByProgramName()
  }

  getCommissionByProgramName() {
    this.commissionService.getCommissionByStudentInfo(this.programName).subscribe({
      next: (res) => {
        this.commissionList = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
