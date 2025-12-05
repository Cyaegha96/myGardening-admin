
import {Admin} from "@/shared/shadcn/components/admin";
import {authProvider} from "@/app/providers/authProvider.ts";
import {dataProvider} from "@/app/providers/mygardeningProvider.ts";
import {LoginPage} from "@/pages/home/LoginPage.tsx";
import {Dashboard} from "@/features/dashboard/dashboard.tsx";
import {Resource} from "ra-core";
import {StickyNoteIcon, UsersIcon, Flower2, ActivityIcon,} from "lucide-react";
import {UserEdit, UserList, UserShow} from "@/features/users/users.tsx";
import {PlantCreate, PlantEdit, PlantList, PlantShow} from "@/features/plants/plants.tsx";
import {SessionList} from "@/features/session/session.tsx";
import { BoardShow} from "@/features/boards/boards.tsx";
import {BoardList} from "@/features/boards/BoardList.tsx";
import {BoardEdit} from "@/features/boards/BoardEdit.tsx";

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

      </Admin>
  )
}

export default App
