
import {Admin, } from "@/shared/shadcn/components/admin";
import {authProvider} from "@/app/providers/authProvider.ts";
import {dataProvider} from "@/app/providers/mygardeningProvider.ts";
import {LoginPage} from "@/pages/home/LoginPage.tsx";
import {Dashboard} from "@/features/dashboard/dashboard.tsx";
import {Resource} from "ra-core";
import {StickyNoteIcon, UsersIcon, Flower2, ActivityIcon, MegaphoneIcon, Flower,} from "lucide-react";
import {UserEdit, UserList, UserShow} from "@/features/users/users.tsx";
import {PlantCreate, PlantEdit, PlantList, PlantShow} from "@/features/plants/plants.tsx";
import {SessionList} from "@/features/session/session.tsx";
import { BoardShow} from "@/features/boards/boards.tsx";
import {BoardList} from "@/features/boards/BoardList.tsx";
import {BoardEdit} from "@/features/boards/BoardEdit.tsx";
import {ReportList} from "@/features/report/ReportList.tsx";
import {ReportShow} from "@/features/report/ReportShow.tsx";
import {ReportEdit} from "@/features/report/ReportEdit.tsx";
import {PlantinforequestList} from "@/features/report/PlantInfoRequestList.tsx";
import {PlantinforequestShow} from "@/features/report/PlantInfoRequestShow.tsx";
import {PlantinforequestEdit} from "@/features/report/PlantInfoRequestEdit.tsx";
import {PotlistingreportList} from "@/features/report/PotListingReportList.tsx";
import {PotListingReportShow} from "@/features/report/PotListingReportShow.tsx";
import {PotlistingreportEdit} from "@/features/report/PotListingReportEdit.tsx";

function App() {

  return (
      <Admin
          authProvider={authProvider}
          dataProvider={dataProvider}
          loginPage={LoginPage}
          dashboard={Dashboard}
          requireAuth>

          <Resource name="users"
                    icon={UsersIcon}
                    list={UserList}
                    show={ UserShow}
                    edit={UserEdit}
          />
          <Resource
            name="plants"
            icon={Flower2}
            list={PlantList}
            show={PlantShow}
            edit={PlantEdit}
            create={PlantCreate}
          />
          <Resource
              name="sessions"
              icon={ActivityIcon}
              list={SessionList}
          />
          <Resource
              name="boards"
              icon={StickyNoteIcon}
              list={BoardList}
              show={BoardShow}
              edit={BoardEdit}
          />
          <Resource
              name="report"
                icon={MegaphoneIcon}
              list={ReportList}
             show={ReportShow}
              edit={ReportEdit}
              // create={ReportCreate}

          />
          <Resource
          name="plantinforequest"
          icon={Flower}
          list={PlantinforequestList}
          show={PlantinforequestShow}
          edit={PlantinforequestEdit}
          />
          <Resource
              name="potlistingreport"
              list={PotlistingreportList}
              show={PotListingReportShow}
              edit={PotlistingreportEdit}
          />
      </Admin>
  )
}

export default App
