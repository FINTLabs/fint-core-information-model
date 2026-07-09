using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Utdanning.Basisklasser;

namespace FINT.Model.Utdanning.Timeplan
{

    public class FagResource : Gruppe 
    {

    
        public FagResource()
        {
            Links = new Dictionary<string, List<Link>>();
        }

        [JsonProperty(PropertyName = "_links")]
        public Dictionary<string, List<Link>> Links { get; private set; }

        protected void AddLink(string key, Link link)
        {
            if (!Links.ContainsKey(key))
            {
                Links.Add(key, new List<Link>());
            }
            Links[key].Add(link);
        }
     
            

        public void AddGrepreferanse(Link link)
        {
            AddLink("grepreferanse", link);
        }

        public void AddSkole(Link link)
        {
            AddLink("skole", link);
        }

        public void AddVigoreferanse(Link link)
        {
            AddLink("vigoreferanse", link);
        }

        public void AddTilrettelegging(Link link)
        {
            AddLink("tilrettelegging", link);
        }

        public void AddProgramomrade(Link link)
        {
            AddLink("programomrade", link);
        }

        public void AddFaggruppe(Link link)
        {
            AddLink("faggruppe", link);
        }

        public void AddUndervisningsgruppe(Link link)
        {
            AddLink("undervisningsgruppe", link);
        }

        public void AddEksamensgruppe(Link link)
        {
            AddLink("eksamensgruppe", link);
        }
    }
}
